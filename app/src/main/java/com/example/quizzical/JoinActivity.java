package com.example.quizzical;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
                    Toast.makeText(JoinActivity.this, "Please enter a game code", Toast.LENGTH_SHORT).show();
                    Log.w("Game Joining", "A code must be introduced.");
                } else {
                    Toast.makeText(JoinActivity.this, "Game code entered: " + gameCode, Toast.LENGTH_SHORT).show();
                    Log.i("Game Joining", "Trying to join to port "+gameCode+"...");
                    //GO TO GAME LOBBY MAYBE? also add if code is wrong
                    finish();
                }
            }
        });
    }
}