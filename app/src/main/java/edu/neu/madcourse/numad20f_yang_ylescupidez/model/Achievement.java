package edu.neu.madcourse.numad20f_yang_ylescupidez.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Achievement {
    private String name;
    private String description;
    @Exclude
    private boolean achieved;
    @Exclude
    private String key;

    private Map<String, Boolean> owners = new HashMap<>();

    public Achievement() {}

    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAchieved() {
        return achieved;
    }

    public void setAchieved(boolean achieved) {
        this.achieved = achieved;
    }

    public Map<String, Boolean> getOwners() {
        return owners;
    }

    public boolean achievedByUser(String userKey) {
        return owners.containsKey(userKey);
    }

    public void addOwner(String userKey) {
        owners.put(userKey, true);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
