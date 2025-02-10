package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "JoinActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        EditText gameCodeInput = findViewById(R.id.code);
        Button continueButton = findViewById(R.id.next);

        continueButton.setOnClickListener(v -> {
            String gameCode = gameCodeInput.getText().toString().trim();

            if (gameCode.isEmpty()) {
                Toast.makeText(JoinActivity.this, R.string.enterCodeToast, Toast.LENGTH_SHORT).show();
                Log.w(TAG, "A code must be introduced.");
                return;
            }

            try {
                int port = Integer.parseInt(gameCode);
                String serverIP = "192.168.1.144"; // Change this to dynamic input if needed

                new Thread(() -> {
                    final GameClient client = GameClient.getInstance(serverIP, port); // Initialize and connect

                    if (client != null && client.isConnected()) {
                        runOnUiThread(() -> {
                            Toast.makeText(JoinActivity.this, R.string.connectedToast, Toast.LENGTH_SHORT).show();
                            Log.i(TAG, "Connected to server on port " + port);

                            Intent intent = new Intent(JoinActivity.this, LobbyActivity.class);
                            intent.putExtra("isHost", false);
                            intent.putExtra("difficulty", 0); // You might not need to pass difficulty here
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(JoinActivity.this, R.string.connectFailedToast, Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Couldn't connect to port " + port);
                        });
                    }
                }).start();
            } catch (NumberFormatException e) {
                Toast.makeText(JoinActivity.this, "Invalid port value", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Invalid port: " + e.getMessage());
            }
        });
    }
}