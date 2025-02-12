package com.example.quizzical.server;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.net.*;

public class GameClient {
    private static final String TAG = "GameClient"; // Tag for logging

    private static GameClient instance;
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private List<String> playerNames = new ArrayList<>(); // Store player names
    private Handler mainHandler;
    private int currentPosition = 0;
    private boolean isMyTurn = false;
    private int myPlayerId;

    private OnMessageReceivedListener messageListener;
    private OnPlayerJoinListener playerJoinListener;
    private OnQuestionReceivedListener questionListener;
    private OnConnectionEstablishedListener connectionEstablishedListener;
    private OnGameStartListener gameStartListener;


    private static final Scanner scanner = new Scanner(System.in); // Global scanner  (Remove in production)

    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }

    // Interface to handle player join events
    public interface OnPlayerJoinListener {
        void onPlayerJoin(String playerName);
        void onPlayerListReceived(List<String> playerNames); // New method
    }

    // Interface to handle received questions
    public interface OnQuestionReceivedListener {
        void onQuestionReceived(String question, String[] answers, int correctIndex);
    }



    public interface OnConnectionEstablishedListener {
        void onConnectionEstablished();
    }

    public interface OnGameStartListener {
        void onGameStart();
    }

    public static GameClient getInstance(String serverAddress, int serverPort) {
        if (instance == null || !instance.isConnected()) {
            instance = new GameClient(serverAddress, serverPort);
            if (!instance.connect()) {
                instance = null; // Reset instance if connection fails
            }
        }
        return instance;
    }

    public static GameClient getInstance() {
        if (instance == null) {
            throw new IllegalStateException("GameClient not initialized. Call getInstance(address, port) first.");
        }
        return instance;
    }


    private GameClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }

    /**
     * Establishes a WebSocket connection to the server.
     *
     * @return Returns true if connected, false if failed.
     */
    public boolean connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            mainHandler = new Handler(Looper.getMainLooper()); // Get the main thread's Handler

            new Thread(this::listenForMessages).start(); // Start listening for server messages
            Log.d(TAG, "Connected to server: " + serverAddress + ":" + serverPort);

            if (connectionEstablishedListener != null) {
                mainHandler.post(() -> connectionEstablishedListener.onConnectionEstablished());            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Connection failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Sends message to server
     *
     * @param message Message.
     */
    public void sendMessage(String message) {
        if (out != null && isConnected()) {
            out.println(message);
            Log.d(TAG, "Sent message: " + message);
        } else {
            Log.w(TAG, "Message not sent. Client is not connected.");
        }
    }

    /**
     * Closes server connection
     */
    public void closeConnection() {
        try {
            if (socket != null) {
                socket.close();
                Log.d(TAG, "Connection closed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error closing connection: " + e.getMessage());
        } finally {
            socket = null;
            instance = null;
        }
    }

    /**
     * Listens for messages.
     */
    private void listenForMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Server: " + message);
                Log.d(TAG, "Received message: " + message);

                String finalMessage = message;

                if (messageListener != null) {
                    mainHandler.post(() -> messageListener.onMessageReceived(finalMessage));
                } else if (message.startsWith("players|")) {
                    mainHandler.post(() -> handlePlayerListMessage(finalMessage));
                } else if (message.startsWith("Player ")) { // Handle join/leave
                    mainHandler.post(() -> handlePlayerUpdateMessage(finalMessage));
                } else if (message.startsWith("question|")) {
                    mainHandler.post(() -> handleQuestionMessage(finalMessage));
                } else if (message.equals("Start".trim())){
                    if (gameStartListener != null) {
                        mainHandler.post(() -> gameStartListener.onGameStart());
                    }
                } else if(message.startsWith("id|")){
                    myPlayerId = Integer.parseInt(message.substring(3));
                    setMyPlayerId(myPlayerId);
                    Log.d(TAG, "My player ID: " + myPlayerId);
                }else if(message.startsWith("turn|")){
                    int playerId = Integer.parseInt(message.substring(5));
                    isMyTurn = (playerId == myPlayerId);
                    if(messageListener != null){
                        mainHandler.post(() -> messageListener.onMessageReceived(finalMessage));
                    }
                    if(isMyTurn){
                        mainHandler.post(() -> {
                            if (messageListener != null) {
                                messageListener.onMessageReceived("Your turn!");
                            }
                        });
                    }
                } else if(message.startsWith("move|")){
                    String[] parts = message.split("\\|");
                    int roll = Integer.parseInt(parts[1]);
                    int playerId = Integer.parseInt(parts[2]);

                    if (playerId == myPlayerId) {
                        currentPosition += roll;
                        mainHandler.post(() -> {
                            if (messageListener != null) {
                                messageListener.onMessageReceived("Moved to " + currentPosition);
                            }
                        });
                        sendMessage("question|" + "category" + "|" + "lang");
                    } else {
                        mainHandler.post(() -> {
                            if (messageListener != null) {
                                messageListener.onMessageReceived("Player " + playerId + " moved " + roll + " spaces.");
                            }
                        });
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server.");
            Log.e(TAG, "Disconnected from server: " + e.getMessage());
            closeConnection();
        }
    }

    /**
     * Handles questions received from the server.
     *
     * @param message Question.
     */
    private void handleQuestionMessage(String message) {
        try {
            // Ensure message format is correct
            String[] parts = message.split("\\|");

            // Ensure message format is correct
            if (parts.length == 4) {
                String question = parts[1];
                String[] answers = parts[2].split(",");
                int correctIndex = Integer.parseInt(parts[3]);

                if (questionListener != null) {
                    questionListener.onQuestionReceived(question, answers, correctIndex);
                }
            } else {
                System.out.println("Invalid question format: " + message);
                Log.w(TAG, "Invalid question format: " + message);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "Error parsing correct index: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error handling question: " + e.getMessage());
        }
    }

    private void handlePlayerListMessage(String message) {
        String playerListStr = message.substring("players|".length());
        String[] players = playerListStr.split(",");
        playerNames.clear(); // Clear existing list

        for (String player : players) {
            if (!player.isEmpty()) { // Check for empty strings
                playerNames.add(player);
            }
        }

        if (playerJoinListener != null) {
            playerJoinListener.onPlayerListReceived(playerNames);
        }
    }

    private void handlePlayerUpdateMessage(String message) {
        String playerName = message;
        if (message.endsWith("joined")) {
            playerName = playerName.substring(7, playerName.length() - 7).trim(); // Extract name and trim
            playerNames.add(playerName);
        } else if (message.endsWith("left")) {
            playerName = playerName.substring(7, playerName.length() - 5).trim(); // Extract name and trim
            playerNames.remove(playerName);
        }

        if (playerJoinListener != null) {
            playerJoinListener.onPlayerListReceived(playerNames);
        }
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.messageListener = listener;
    }

    public void setOnPlayerJoinListener(OnPlayerJoinListener listener) {
        this.playerJoinListener = listener;
    }

    public void setOnQuestionReceivedListener(OnQuestionReceivedListener listener) {
        this.questionListener = listener;
    }


    public void setOnConnectionEstablishedListener(OnConnectionEstablishedListener listener) {
        this.connectionEstablishedListener = listener;
    }

    public OnConnectionEstablishedListener getOnConnectionEstablishedListener() {
        return this.connectionEstablishedListener;
    }

    public void setOnGameStartListener(OnGameStartListener listener) {
        this.gameStartListener = listener;
    }

    public int getMyPlayerId() { // Getter for myPlayerId
        return myPlayerId;
    }

    public void setMyPlayerId(int myPlayerId) { // Setter for myPlayerId
        this.myPlayerId = myPlayerId;
    }
    public int getCurrentPosition() {
        return currentPosition;
    }
    public void setCurrentPosition() {
        this.currentPosition = currentPosition;
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    // Testing server (Remove in production)
    public static void main(String[] args) {
        System.out.print("Enter server IP: ");
        String serverIP = scanner.nextLine();

        System.out.print("Enter server port: ");
        int port = scanner.nextInt();
        scanner.nextLine();

        GameClient client = new GameClient(serverIP, port);

        if (client.connect()) {
            System.out.println("Connected to the game server!");

            new Thread(() -> {
                while (true) {
                    String message = scanner.nextLine();
                    if (message.equalsIgnoreCase("exit")) {
                        client.closeConnection();
                        System.out.println("Disconnected from server.");
                        break;
                    }
                    client.sendMessage(message);
                }
            }).start();
        } else {
            System.out.println("Failed to connect to server.");
        }
    }
}