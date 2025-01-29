package com.example.quizzical.server;

import android.util.Log;

import java.io.*;
import java.net.Socket;

public class GameClient implements Serializable {
    private String serverAddress;
    private int serverPort;
    private transient Socket socket;
    private transient BufferedReader in;
    private transient PrintWriter out;
    private transient OnMessageReceivedListener messageListener;
    private transient OnPlayerJoinListener playerJoinListener;

    public interface OnMessageReceivedListener {
        void onMessageReceived(String message);
    }

    public interface OnPlayerJoinListener {
        void onPlayerJoin(String playerName);
    }

    public GameClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void setOnMessageReceivedListener(OnMessageReceivedListener listener) {
        this.messageListener = listener;
    }

    public void setOnPlayerJoinListener(OnPlayerJoinListener listener) {
        this.playerJoinListener = listener;
    }

    public boolean connect() {
        try {
            socket = new Socket(serverAddress, serverPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            Log.i("GameClient", "Connected to server on port " + serverPort);
            new Thread(this::listenForMessages).start();
            return true;
        } catch (IOException e) {
            Log.e("GameClient", "Failed to connect to server: " + e.getMessage());
            return false;
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
            Log.i("GameClient", "Sent: " + message);
        }
    }

    private void listenForMessages() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                Log.i("GameClient", "Received: " + message);
                if (messageListener != null) {
                    messageListener.onMessageReceived(message);
                }
                if (message.startsWith("Player")) {
                    if (playerJoinListener != null) {
                        Log.d("GameClient", "Notifying player join: " + message);
                        playerJoinListener.onPlayerJoin(message);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("GameClient", "Error receiving message: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (socket != null) socket.close();
            Log.i("GameClient", "Disconnected from server.");
        } catch (IOException e) {
            Log.e("GameClient", "Error closing connection: " + e.getMessage());
        }
    }
}