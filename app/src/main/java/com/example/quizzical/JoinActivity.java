package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

public class JoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        EditText gameCodeInput = findViewById(R.id.code);
        Button continueButton = findViewById(R.id.next);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameCode = gameCodeInput.getText().toString().trim();

                if (gameCode.isEmpty()) {
                    Toast.makeText(JoinActivity.this, R.string.enterCodeToast, Toast.LENGTH_SHORT).show();
                    Log.w("Game Joining", "A code must be introduced.");
                } else {
                    try {
                        int port = Integer.parseInt(gameCode);

                        new Thread(() -> {
                            GameClient client = new GameClient("192.168.1.245", port);
                            boolean isConnected = client.connect();

                            runOnUiThread(() -> {
                                if (isConnected) {
                                    Toast.makeText(JoinActivity.this, R.string.connectedToast, Toast.LENGTH_SHORT).show();
                                    Log.i("Game Joining", "Connected to server on port " + port);

                                    Intent intent = new Intent(JoinActivity.this, LobbyActivity.class);
                                    intent.putExtra("client", client);
                                    intent.putExtra("isHost", false);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(JoinActivity.this, R.string.connectFailedToast, Toast.LENGTH_SHORT).show();
                                    Log.e("Game Joining", "Couldn't connect to port " + port);
                                }
                            });
                        }).start();
                    } catch (NumberFormatException e) {
                        Toast.makeText(JoinActivity.this, "Invalid port value", Toast.LENGTH_SHORT).show();
                        Log.e("Game Joining", "Invalid port: " + e.getMessage());
                    }
                }
            }
        });
    }
}