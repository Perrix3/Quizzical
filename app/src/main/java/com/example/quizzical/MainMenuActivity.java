package com.example.quizzical;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    private LinearLayout menu;
    private boolean isMenuVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menu = findViewById(R.id.slidingMenu);
        ImageButton menuButton = findViewById(R.id.menuButton);
        ImageButton closeButton = findViewById(R.id.closeButton);

        RecyclerView recyclerView = findViewById(R.id.gameHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Test data
        List<GameHistoryClass> gameHistory = new ArrayList<>();
        gameHistory.add(new GameHistoryClass("19/01/2025", "Jeff", "Victory", "10 mins."));
        gameHistory.add(new GameHistoryClass("20/01/2025", "Jester", "Defeat", "15 mins."));
        gameHistory.add(new GameHistoryClass("21/01/2025", "Skibidi", "Victory", "8 mins."));

        GameHistoryAdapter adapter = new GameHistoryAdapter(this, gameHistory);
        recyclerView.setAdapter(adapter);

        menuButton.setOnClickListener(view -> toggleMenu());
        closeButton.setOnClickListener(view -> toggleMenu());


        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button statsButton = findViewById(R.id.statsButton);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainMenuActivity.this, StatsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void toggleMenu() {
        if (isMenuVisible) {
            //Open
            TranslateAnimation slideOut = new TranslateAnimation(0, -menu.getWidth(), 0, 0);
            slideOut.setDuration(300);
            slideOut.setFillAfter(false);
            slideOut.setAnimationListener(new AnimationEndListener(() -> menu.setVisibility(View.GONE)));
            menu.startAnimation(slideOut);
        } else {
            //Close
            menu.setVisibility(View.VISIBLE);
            TranslateAnimation slideIn = new TranslateAnimation(-menu.getWidth(), 0, 0, 0);
            slideIn.setDuration(300);
            slideIn.setFillAfter(true);
            menu.startAnimation(slideIn);
        }
        isMenuVisible = !isMenuVisible;
    }

    private class AnimationEndListener implements android.view.animation.Animation.AnimationListener {
        private final Runnable onEnd;

        public AnimationEndListener(Runnable onEnd) {
            this.onEnd = onEnd;
        }

        @Override
        public void onAnimationEnd(android.view.animation.Animation animation) {
            onEnd.run();
        }

        @Override
        public void onAnimationRepeat(android.view.animation.Animation animation) {}

        @Override
        public void onAnimationStart(android.view.animation.Animation animation) {}
    }
}
