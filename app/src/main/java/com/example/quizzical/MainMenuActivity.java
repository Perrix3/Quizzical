package com.example.quizzical;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        RecyclerView recyclerView = findViewById(R.id.gameHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Test data
        List<GameHistoryClass> gameHistory = new ArrayList<>();
        gameHistory.add(new GameHistoryClass("19/01/2025", "Jeff", "Victory", "10 mins."));
        gameHistory.add(new GameHistoryClass("20/01/2025", "Jester", "Defeat", "15 mins."));
        gameHistory.add(new GameHistoryClass("21/01/2025", "Skibidi", "Victory", "8 mins."));

        GameHistoryAdapter adapter = new GameHistoryAdapter(this, gameHistory);
        recyclerView.setAdapter(adapter);
    }
}
