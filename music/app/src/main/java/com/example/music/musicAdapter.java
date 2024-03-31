package com.example.music;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class musicAdapter extends RecyclerView.Adapter<musicAdapter.ViewHolder> {


    ArrayList<AudioModel> songsList;
    Context context;

    public musicAdapter(ArrayList<AudioModel> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new musicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(musicAdapter.ViewHolder holder , int position) {
        AudioModel songData = songsList.get(position);
        holder.tittleTextView.setText(songData.getTittle());

        if (MyMediaPlayer.currentIndex == position){
            holder.tittleTextView.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.tittleTextView.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context,MusicPlayerActivity.class);
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tittleTextView;
        ImageView iconImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            tittleTextView = itemView.findViewById(R.id.music_tittle_text);
            iconImageView = itemView.findViewById(R.id.icon_view);
        }
    }
}
