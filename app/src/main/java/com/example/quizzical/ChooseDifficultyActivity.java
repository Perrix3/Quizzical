package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseDifficultyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        Button easyButton = findViewById(R.id.easy);
        Button mediumButton = findViewById(R.id.medium);
        Button hardButton = findViewById(R.id.hard);



        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ChooseDifficultyActivity.this, JoinActivity.class);
                //startActivity(intent);
                finish();
            }
        });



        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ChooseDifficultyActivity.this, HostGameActivity.class);
                //startActivity(intent);
                finish();
            }
        });



        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ChooseDifficultyActivity.this, JoinActivity.class);
                //startActivity(intent);
                finish();
            }
        });

    }
}
