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

public class EnterPortActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_port);

        EditText gameCodeInput=findViewById(R.id.code);
        Button continueButton=findViewById(R.id.next);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int port;
                String gameCode=gameCodeInput.getText().toString().trim();
                port=Integer.parseInt(gameCode);
                if (gameCode.isEmpty()) {
                    Toast.makeText(EnterPortActivity.this, R.string.enterCodeToast, Toast.LENGTH_SHORT).show();
                    Log.w("Game Hosting", "A port must be introduced.");
                } else if (port<5000 || port>6000) {
                    Toast.makeText(EnterPortActivity.this, R.string.rangePort, Toast.LENGTH_SHORT).show();
                    Log.w("Game Hosting", "Port must be between 5000-6000.");
                } else{
                    Log.i("Game Hosting", "Port introduced correctly: "+port);
                    Intent intent = new Intent(EnterPortActivity.this, ChooseDifficultyActivity.class);
                    intent.putExtra("port",port);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
