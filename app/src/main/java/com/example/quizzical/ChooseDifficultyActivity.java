package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

public class ChooseDifficultyActivity extends AppCompatActivity {
    private final int port = 8089;
    private final String serverIP = "192.168.1.144"; // Change this to your actual server IP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        Button easyButton = findViewById(R.id.easy);
        Button mediumButton = findViewById(R.id.medium);
        Button hardButton = findViewById(R.id.hard);

        easyButton.setOnClickListener(v -> startGame(1));
        mediumButton.setOnClickListener(v -> startGame(2));
        hardButton.setOnClickListener(v -> startGame(3));
    }

    private void startGame(int difficulty) {
        new Thread(() -> {
            GameClient client = new GameClient(serverIP, port);
            boolean connected = client.connect();

            if (connected) {
                runOnUiThread(() -> {
                    Intent intent = new Intent(ChooseDifficultyActivity.this, LobbyActivity.class);
                    intent.putExtra("isHost", true);
                    intent.putExtra("port", port);
                    intent.putExtra("difficulty", difficulty);
                    intent.putExtra("serverIP", serverIP);
                    startActivity(intent);
                    finish();
                });
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Failed to connect to server", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
