package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.achievements;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import edu.neu.madcourse.numad20f_yang_ylescupidez.db.Repo;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Achievement;

public class AchievementsViewModel extends ViewModel {

    private LiveData<List<Achievement>> achievements;
    public LiveData<List<Achievement>> getAchievements() {
        if (achievements == null) {
            achievements = Repo.getInstance().getAchievements();
        }
        return achievements;
    }
}