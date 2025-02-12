package com.example.quizzical;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

import java.util.Locale;

public class GameBoardActivity extends AppCompatActivity {

    private GridLayout boardGrid;
    private TextView[] tiles;
    private int numRows = 3;
    private int numCols = 7;
    private Button rollButton;
    private Button endTurnButton;
    private Button endGameButton;
    private boolean isTurn = false;
    private GameClient client;
    private int myPlayerId;
    private int currentPosition = 0; // Keep track of current position
    private boolean waitingForAnswer = false;
    private boolean finalQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        boardGrid = findViewById(R.id.board_grid);
        rollButton = findViewById(R.id.roll_dice_button);
        endTurnButton = findViewById(R.id.end_turn_button);
        endGameButton = findViewById(R.id.end_game_button);

        boardGrid.setRowCount(numRows);
        boardGrid.setColumnCount(numCols);

        tiles = new TextView[numRows * numCols];

        for (int i = 0; i < numRows * numCols; i++) {
            TextView tile = new TextView(this);
            tile.setBackgroundColor(Color.LTGRAY);
            tile.setPadding(2, 2, 2, 2);
            tile.setGravity(Gravity.CENTER);
            tile.setText(String.valueOf(i + 1)); // Tile number for debugging
            tile.setTextColor(Color.BLACK);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.rowSpec = GridLayout.spec(i / numCols, 1, 1f);
            params.columnSpec = GridLayout.spec(i % numCols, 1, 1f);
            params.width = 0;
            params.height = 0;

            // Important: Set margins for spacing between tiles
            int margin = 4; // Adjust as needed
            params.setMargins(margin, margin, margin, margin);

            tile.setLayoutParams(params);
            boardGrid.addView(tile);
            tiles[i] = tile;
        }

        client = GameClient.getInstance();

        client.setOnMessageReceivedListener(message -> {
            if (message.startsWith("Your turn!")) {
                isTurn = true;
                rollButton.setVisibility(View.VISIBLE);
                endTurnButton.setVisibility(View.VISIBLE);
                Toast.makeText(this, R.string.uTurn, Toast.LENGTH_SHORT).show();
            } else if (message.startsWith("Moved to ")) {
                int newPosition = Integer.parseInt(message.substring(9));
                updatePlayerPosition(newPosition);

                requestTriviaQuestion();
                if(newPosition == tiles.length){ // If last question turn it true, so if it's correct, end game
                    finalQuestion=true;
                }

            } else if (message.startsWith("Player ") && message.endsWith("rolled ")) {
                // Handle other players' rolls later !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! REMEMBER
            } else if (message.startsWith("turn|")) {
                int playerId = Integer.parseInt(message.substring(5));
                String turnPlayer = "";
                for (String playerName : client.getPlayerNames()) {
                    if (playerName.contains("Player " + playerId)) {
                        turnPlayer = playerName;
                        break;
                    }
                }
            }else if (message.startsWith("question|")) {
                handleQuestion(message);
            }
        });

        rollButton.setOnClickListener(v -> {
            if (isTurn) {
                client.sendMessage("roll");
                isTurn = false;
                rollButton.setVisibility(View.GONE);
                endTurnButton.setVisibility(View.GONE);
            }
        });

        endTurnButton.setOnClickListener(v -> {
            if (isTurn) {
                client.sendMessage("endTurn");
                isTurn = false;
                rollButton.setVisibility(View.GONE);
                endTurnButton.setVisibility(View.GONE);
            }
        });

    }
    private void updatePlayerPosition(int newPosition) {
        if (currentPosition > 0 && currentPosition <= tiles.length) {
            tiles[currentPosition - 1].setBackgroundColor(Color.LTGRAY);
        }
        currentPosition = newPosition;

        if (newPosition > 0 && newPosition <= tiles.length) {
            tiles[newPosition - 1].setBackgroundColor(Color.GREEN);
        } else {
            Log.e("GameBoard", "Invalid position: " + newPosition);
        }
    }

    private void requestTriviaQuestion() {
        Locale currentLocale = Locale.getDefault();
        String languageCode = currentLocale.getLanguage();

        if (!waitingForAnswer) {
            waitingForAnswer = true;
            String category = "random";
            client.sendMessage("|question|" + category + "|" + languageCode);
        }
    }

    private void handleQuestion(String message) {
        try {
            String[] parts = message.split("\\|");
            String question = parts[1];
            String[] answers = parts[2].split(",");
            int correctIndex = Integer.parseInt(parts[3]);

            runOnUiThread(() -> {
                showQuestionDialog(question, answers, correctIndex);
            });

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            Log.e("GameBoard", "Error parsing question message: " + e.getMessage());
            waitingForAnswer = false;
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

    private void showQuestionDialog(String question, String[] answers, int correctIndex) {
        if (isFinishing()) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(question);

        View dialogView = getLayoutInflater().inflate(R.layout.question_dialog_layout, null);
        builder.setView(dialogView);

        TextView questionText = dialogView.findViewById(R.id.question_text);
        Button answer1Button = dialogView.findViewById(R.id.answer1_button);
        Button answer2Button = dialogView.findViewById(R.id.answer2_button);
        Button answer3Button = dialogView.findViewById(R.id.answer3_button);
        Button answer4Button = dialogView.findViewById(R.id.answer4_button);

        questionText.setText(question);

        if (answers.length >= 4) { // Only set text if there are enough answers
            answer1Button.setText(answers[0]);
            answer2Button.setText(answers[1]);
            answer3Button.setText(answers[2]);
            answer4Button.setText(answers[3]);
        } else {
            Log.e("GameBoard", "Not enough answers provided: " + answers.length);
            waitingForAnswer = false; // Reset waiting flag
            return; // Exit to prevent errors
        }


        AlertDialog dialog = builder.create();

        View.OnClickListener answerListener = v -> {
            int selectedAnswerIndex = -1;
            if (v == answer1Button) selectedAnswerIndex = 0;
            else if (v == answer2Button) selectedAnswerIndex = 1;
            else if (v == answer3Button) selectedAnswerIndex = 2;
            else if (v == answer4Button) selectedAnswerIndex = 3;

            if (selectedAnswerIndex == correctIndex) {
                Toast.makeText(this, R.string.correct, Toast.LENGTH_SHORT).show();
                waitingForAnswer = false;

                if (currentPosition == tiles.length) {
                    Toast.makeText(this, R.string.win, Toast.LENGTH_SHORT).show();
                    client.sendMessage("win");
                    // WIN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                }

            } else {
                Toast.makeText(this, R.string.incorrect, Toast.LENGTH_SHORT).show();

                int roll = (int) (Math.random() * 6) + 1; // Simulate the roll again
                currentPosition -= roll; // Go back the amount rolled
                if (currentPosition < 1) currentPosition = 1;

                updatePlayerPosition(currentPosition);
                waitingForAnswer = false;
            }

            dialog.dismiss();

        };

        answer1Button.setOnClickListener(answerListener);
        answer2Button.setOnClickListener(answerListener);
        answer3Button.setOnClickListener(answerListener);
        answer4Button.setOnClickListener(answerListener);

        dialog.setCanceledOnTouchOutside(false); // Prevent dismiss on outside touch
        dialog.show();

    }
}
