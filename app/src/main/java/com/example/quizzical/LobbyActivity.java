package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
    private RadioGroup dif;
    private RadioButton easy;
    private RadioButton medium;
    private RadioButton hard;
    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // For network operations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        playersListView = findViewById(R.id.list);
        startGameButton = findViewById(R.id.start);
        dif = findViewById(R.id.difficulty);
        easy = findViewById(R.id.easy);
        medium = findViewById(R.id.medium);
        hard = findViewById(R.id.hard);

        // Get the GameClient instance. No longer getting from Intent
        client = GameClient.getInstance();

        // Check if client is initialized and connected
        if (client == null || !client.isConnected()) {
            Log.e(TAG, "GameClient is not initialized or not connected. Finishing activity.");
            finish();
            return;
        }

        playerNames = new ArrayList<>(); // Initialize the list


        startGameButton.setOnClickListener(v -> {
            // Go to game
            int difficulty = getSelectedDifficulty();
            if (difficulty == 0) {
                Toast.makeText(LobbyActivity.this, R.string.chooseDif, Toast.LENGTH_SHORT).show();
                return;
            }else{
                Log.d(TAG, "Starting the game with difficulty: " + difficulty);
                executorService.execute(() -> {
                    client.sendMessage("start|" + difficulty);
                });
            }
        });


        client.setOnPlayerJoinListener(new GameClient.OnPlayerJoinListener() {
            @Override
            public void onPlayerJoin(String playerName) {
                Log.d(TAG, "New player joined: " + playerName);
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

        client.setOnGameStartListener(new GameClient.OnGameStartListener() {
            @Override
            public void onGameStart() {
                Log.d(TAG, "Received 'Start' from server. Starting GameBoardActivity.");
                runOnUiThread(() -> { // Ensure UI updates are on the main thread
                    Intent intent = new Intent(LobbyActivity.this, GameBoardActivity.class);
                    startActivity(intent);
                    finish(); // Finish LobbyActivity to prevent going back
                });
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                client.sendMessage("getPlayers"); // Request initial player list from the server
            }
        });

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(LobbyActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
            return;
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

    private int getSelectedDifficulty() {
        if (easy.isChecked()) {
            return 1;
        } else if (medium.isChecked()) {
            return 2;
        } else if (hard.isChecked()) {
            return 3;
        } else {
            return 0;
        }
    }
}