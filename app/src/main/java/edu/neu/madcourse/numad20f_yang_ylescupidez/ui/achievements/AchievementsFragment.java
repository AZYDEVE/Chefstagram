package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.achievements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterAchievement;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Achievement;

public class AchievementsFragment extends Fragment {
    private AchievementsViewModel model;

    private RecyclerView recyclerView;
    private RecyclerAdapterAchievement rAdapter;

    public AchievementsFragment() {}

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_achievements, container, false);
        model =
                ViewModelProviders.of(this).get(AchievementsViewModel.class);
        model.getAchievements().observe(this, new Observer<List<Achievement>>() {
            @Override
            public void onChanged(List<Achievement> achievements) {
                rAdapter.notifyDataSetChanged();
            }
        });
        recyclerView = root.findViewById(R.id.achievements_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        rAdapter = new RecyclerAdapterAchievement(model.getAchievements().getValue());
        recyclerView.setAdapter(rAdapter);
        return root;
    }
}