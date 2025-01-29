package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;
import com.example.quizzical.server.GameServer;

public class ChooseDifficultyActivity extends AppCompatActivity {
    private GameServer gameServer;
    private int port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        Button easyButton = findViewById(R.id.easy);
        Button mediumButton = findViewById(R.id.medium);
        Button hardButton = findViewById(R.id.hard);

        Intent intent = getIntent();
        port = intent.getIntExtra("port", 0);
        gameServer = new GameServer(port);

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(1);
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(2);
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame(3);
            }
        });

    }

    private void startGame(int difficulty) {
        new Thread(() -> {
            gameServer.start();
        }).start();
        new Thread(() -> {
            GameClient client = new GameClient("192.168.1.245", port);
            client.connect();
            Intent intent = new Intent(ChooseDifficultyActivity.this, LobbyActivity.class);
            intent.putExtra("isHost", true);
            intent.putExtra("port", port);
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("client", client);
            startActivity(intent);
            finish();
        }).start();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}