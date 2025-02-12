package com.example.quizzical;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

public class GameBoardActivity extends AppCompatActivity {

    private GridLayout boardGrid;
    private GameClient client;
    private TextView[] tiles; // Array to hold tile TextViews

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        client = GameClient.getInstance();


        boardGrid = findViewById(R.id.board_grid);

        // Initialize tiles array (25 tiles for 5x5 grid)
        tiles = new TextView[25];

        // Create board tiles programmatically
        for (int i = 0; i < 25; i++) {
            TextView tile = new TextView(this);
            tile.setBackgroundColor(Color.LTGRAY); // Default tile color
            tile.setWidth(0); // Equal width distribution
            tile.setHeight(0); // Equal height distribution
            tile.setPadding(5, 5, 5, 5);
            tile.setGravity(android.view.Gravity.CENTER);
            tile.setClickable(true); // Make tiles clickable (optional)
            // Add tile to the grid
            boardGrid.addView(tile);

            // Store tile in the array
            tiles[i] = tile;

            // Optional: Set tile click listener (for future interaction)
            final int tileIndex = i; // Must be final for lambda expression
            tile.setOnClickListener(v -> {
                // Handle tile click here (e.g., for debugging or game logic)
                // You can access the tileIndex to identify which tile was clicked
                // For example:
                // Toast.makeText(this, "Tile " + tileIndex + " clicked", Toast.LENGTH_SHORT).show();
            });
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) { // Check if the activity is actually finishing
            GameClient client = GameClient.getInstance();
            if (client != null) {
                client.closeConnection();
            }
        }
    }
}
