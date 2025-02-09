package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {
    private ListView playersListView;
    private Button startGameButton;
    private boolean isHost;
    private List<String> playerNames;
    private GameClient client;
    private PlayerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        playersListView = findViewById(R.id.list);
        startGameButton = findViewById(R.id.start);

        isHost = getIntent().getBooleanExtra("isHost", false);
        client = (GameClient) getIntent().getSerializableExtra("client");
        playerNames = new ArrayList<>();

        //Add host to player list
        if (isHost) {
            playerNames.add("Host (You)");
        } else {
            playerNames.add("Player (You)");
        }
        Log.d("LobbyActivity", "Updating player list");
        updatePlayerList();

        if (isHost) {
            startGameButton.setVisibility(View.VISIBLE);
            startGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Go to game
                }
            });
        } else {
            startGameButton.setVisibility(View.GONE);
        }

        client.setOnPlayerJoinListener(new GameClient.OnPlayerJoinListener() {
            @Override
            public void onPlayerJoin(String playerName) {
                Log.d("LobbyActivity", "New player joined: " + playerName);
                runOnUiThread(() -> {
                    playerNames.add(playerName);
                    updatePlayerList();
                });
            }
        });
    }

    private void updatePlayerList() {
        Log.d("LobbyActivity", "Updating player list: " + playerNames);
        if (adapter==null) {
            adapter=new PlayerListAdapter(this, playerNames);
            playersListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}