package com.example.quizzical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class gameHistoryAdapter extends RecyclerView.Adapter<gameHistoryAdapter.ViewHolder>{

    private List<String> mData;
    private LayoutInflater mInflater;

    gameHistoryAdapter(Context context, List<String> data){
        this.mInflater=LayoutInflater.from(context);
        this.mData=data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=mInflater.inflate(R.layout.recyclerview_games_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(gameHistoryAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
