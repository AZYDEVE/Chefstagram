package edu.neu.madcourse.numad20f_yang_ylescupidez.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Achievement;

public class RecyclerAdapterAchievement extends RecyclerView.Adapter<RecyclerAdapterAchievement.AchievementRviewHolder> {
    private List<Achievement> achievementList;
    private AchievementClickListener achievementClickListener;


    public interface AchievementClickListener {
        void onAchievementClick(int position);
    }

    public void setList(List<Achievement> list) {
        this.achievementList = list;
    }


    public void setOnAchievementClickListener(AchievementClickListener listener) {
        this.achievementClickListener = listener;
    }


    public RecyclerAdapterAchievement(List<Achievement> achievementList) {
        this.achievementList = achievementList;
    }

    @NonNull
    @Override
    public AchievementRviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_achievement, parent, false);
        return new AchievementRviewHolder(view, achievementClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementRviewHolder holder, int position) {
        Achievement currentAchievement = achievementList.get(position);
        holder.name.setText(currentAchievement.getName());
        holder.description.setText(String.valueOf(currentAchievement.getDescription()));
        int medalIcon = currentAchievement.isAchieved() ? R.drawable.ic_achievement_unlocked : R.drawable.ic_achievement_locked;
        holder.medalIcon.setImageResource(medalIcon);
    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public static class AchievementRviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView description;
        public ImageView medalIcon;
        AchievementClickListener listener;

        public AchievementRviewHolder(@NonNull View achievementView, final AchievementClickListener listener) {
            super(achievementView);
            this.name = achievementView.findViewById(R.id.achievement_title);
            this.description = achievementView.findViewById(R.id.achievement_description);
            this.medalIcon = achievementView.findViewById(R.id.medal_icon);
            this.listener = listener;

            achievementView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onAchievementClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            listener.onAchievementClick(getAdapterPosition());
        }
    }
}
