package com.example.quizzical;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LobbyActivity extends AppCompatActivity {
    private static final String TAG = "LobbyActivity"; // Tag for logging

    private ListView playersListView;
    private Button startGameButton;
    private boolean isHost;
    private List<String> playerNames;
    private GameClient client;
    private PlayerListAdapter adapter;
    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // For network operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        playersListView = findViewById(R.id.list);
        startGameButton = findViewById(R.id.start);

        isHost = getIntent().getBooleanExtra("isHost", false);

        // Get the GameClient instance. No longer getting from Intent
        client = GameClient.getInstance();

        // Check if client is initialized and connected
        if (client == null || !client.isConnected()) {
            Log.e(TAG, "GameClient is not initialized or not connected. Finishing activity.");
            finish(); // Or handle the error as you see fit.
            return;
        }

        playerNames = new ArrayList<>(); // Initialize the list

        if (isHost) {
            startGameButton.setVisibility(View.VISIBLE);
            startGameButton.setOnClickListener(v -> { // Use lambda expression for conciseness
                // Go to game
                Log.d(TAG, "Starting the game!");
                // ... your code to start the game activity ...
            });
        } else {
            startGameButton.setVisibility(View.GONE);
        }

        client.setOnPlayerJoinListener(new GameClient.OnPlayerJoinListener() {
            @Override
            public void onPlayerJoin(String playerName) {
                Log.d(TAG, "New player joined: " + playerName);
                // This callback is now redundant; the full player list is received in onPlayerListReceived
            }

            @Override
            public void onPlayerListReceived(List<String> updatedPlayerNames) {
                Log.d(TAG, "Player list updated: " + updatedPlayerNames);
                runOnUiThread(() -> {
                    playerNames.clear(); // Clear the current list
                    playerNames.addAll(updatedPlayerNames); // Add all players from the server
                    updatePlayerList(); // Update the UI
                });
            }
        });

        executorService.execute(new Runnable() { // Anonymous inner class for network call
            @Override
            public void run() {
                client.sendMessage("getPlayers"); // Request initial player list from the server
            }
        });
    }

    private void updatePlayerList() {
        Log.d(TAG, "Updating player list: " + playerNames);
        if (adapter == null) {
            adapter = new PlayerListAdapter(this, playerNames);
            playersListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // It's crucial to close the connection when the activity is destroyed
        if (isFinishing()) { // Check if the activity is actually finishing
            GameClient client = GameClient.getInstance();
            if (client != null) {
                client.closeConnection();
            }
        }
        executorService.shutdown(); // Important: Shut down the executor service
    }
}