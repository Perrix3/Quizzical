package com.example.quizzical.server;

import android.util.Log;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private static final int MAX_PLAYERS = 6;
    private int port;
    private List<ClientHandler> clients;
    private boolean gameStarted;

    public GameServer(int port) {
        this.port = port;
        this.clients = new ArrayList<>();
        this.gameStarted = false;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Log.i("Host Game", "Game server started on port: " + port);
            Log.i("Host Game", "Waiting for players...");

            while (clients.size() < MAX_PLAYERS && !gameStarted) {
                Socket playerSocket = serverSocket.accept();
                if (clients.size() < MAX_PLAYERS) {
                    ClientHandler clientHandler = new ClientHandler(playerSocket, this);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                    Log.i("Host Game", "Player connected. Total players: " + clients.size());
                    broadcastMessage("Player " + clients.size() + " joined");
                } else {
                    Log.e("Host Game", "Game is full.");
                    playerSocket.close();
                }
            }

            if (!gameStarted) {
                startGame();
            }
        } catch (IOException e) {
            Log.e("Host Game", "Server error: " + e.getMessage());
        }
    }

    private void startGame() {
        Log.i("Host Game", "Starting the game with " + clients.size() + " players...");
        broadcastMessage("Game starting");

        //GAME LOGIC HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        gameStarted = true;
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private GameServer server;

        public ClientHandler(Socket socket, GameServer server) {
            this.clientSocket = socket;
            this.server = server;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    Log.i("GameServer", "Received: " + message);
                    server.broadcastMessage(message);
                }
            } catch (IOException e) {
                Log.e("GameServer", "Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    Log.e("GameServer", "Error closing client socket: " + e.getMessage());
                }
            }
        }

        public void sendMessage(String message) {
            if (out != null) {
                out.println(message);
            }
        }
    }

    //TEST
    public static void main(String[] args) {
        int port=1234;
        GameServer server = new GameServer(port);
        server.start();
    }

}