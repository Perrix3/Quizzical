package com.example.quizzical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PlayerListAdapter extends android.widget.ArrayAdapter<String> {
    public PlayerListAdapter(@NonNull Context context, @NonNull List<String> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.player_list_item, parent, false);
        }

        TextView playerNameTextView = convertView.findViewById(R.id.player_name);
        String playerName = getItem(position);

        playerNameTextView.setText(playerName);

        return convertView;
    }
}