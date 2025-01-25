package com.example.quizzical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GameHistoryAdapter extends RecyclerView.Adapter<GameHistoryAdapter.ViewHolder> {

    private List<GameHistoryClass> mData;
    private LayoutInflater mInflater;

    public GameHistoryAdapter(Context context, List<GameHistoryClass> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_games_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameHistoryClass gh = mData.get(position);

        holder.date.setText(gh.getDate());
        holder.opponent.setText(gh.getOpponent());
        holder.result.setText(gh.getResult());
        holder.time.setText(gh.getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, opponent, result, time;

        ViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            opponent = itemView.findViewById(R.id.opponent);
            result = itemView.findViewById(R.id.result);
            time = itemView.findViewById(R.id.time);
        }
    }
}
