package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PlayGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        Button joinButton = findViewById(R.id.joinButton);
        Button hostButton = findViewById(R.id.hostButton);
        Button cancelButton = findViewById(R.id.cancelButton);


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayGameActivity.this, JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });


        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayGameActivity.this, ChooseDifficultyActivity.class);
                startActivity(intent);
                finish();
            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
