package com.example.quizzical;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quizzical.server.GameClient;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        // Retrieve the passed values from JoinActivity
        Intent intent = getIntent();
        String serverIP = intent.getStringExtra("serverIP");
        int port = intent.getIntExtra("port", -1); // Retrieve the port from the intent

        if (port != -1) {
            // Perform connection in a background thread (using AsyncTask)
            new ConnectToServerTask().execute(serverIP, port);
        } else {
            Toast.makeText(this, "Invalid port", Toast.LENGTH_SHORT).show();
        }
    }

    // AsyncTask to handle network operation off the main thread
    private class ConnectToServerTask extends AsyncTask<Object, Void, Boolean> {
        private int port;

        @Override
        protected Boolean doInBackground(Object... params) {
            String serverIP = (String) params[0];
            port = (int) params[1];  // Save the port for use in onPostExecute()

            GameClient client = new GameClient(serverIP, port);
            return client.connect();  // Perform the connection in the background
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            super.onPostExecute(isConnected);

            if (isConnected) {
                Toast.makeText(LobbyActivity.this, R.string.connectedToast, Toast.LENGTH_SHORT).show();
                Log.i("Game Joining", "Connected to server on port " + port);
                // Proceed to the game
            } else {
                Toast.makeText(LobbyActivity.this, R.string.connectFailedToast, Toast.LENGTH_SHORT).show();
                Log.e("Game Joining", "Couldn't connect to port " + port);
            }
        }
    }
}
