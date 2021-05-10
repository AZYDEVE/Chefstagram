package edu.neu.madcourse.numad20f_yang_ylescupidez.adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


import edu.neu.madcourse.numad20f_yang_ylescupidez.R;

public class RecyclerAdapterAchievementIcon extends RecyclerView.Adapter<RecyclerAdapterAchievementIcon.ViewHolder> {

    private ArrayList<String> iconUrlList;
    private Context mContext;

    public RecyclerAdapterAchievementIcon(ArrayList<String> iconUrlList, Context mContext) {
        this.iconUrlList = iconUrlList;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_profile_achievement,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext).asBitmap().load(iconUrlList.get(position)).into(holder.achievementIcon);

    }

    @Override
    public int getItemCount() {
        return iconUrlList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView achievementIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           achievementIcon =itemView.findViewById(R.id.achievement_icon);


        }
    }
}
