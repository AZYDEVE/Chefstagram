package edu.neu.madcourse.numad20f_yang_ylescupidez.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String userName;
    private String profilePicture;
    private String bio;
    private Map<String, Boolean> createdRecipes = new HashMap<>();
    private Map<String, Boolean> likedRecipes = new HashMap<>();
    private String key;

    public User() {
    }

    public User(String userName, String profilePicture, String bio) {
        this.userName = userName;
        this.profilePicture = profilePicture;
        this.bio = bio;
    }


    public Map<String, Boolean> getCreatedRecipes() {
        return createdRecipes;
    }

    public void setCreatedRecipes(Map<String, Boolean> createdRecipes) {
        this.createdRecipes = createdRecipes;
    }

    public Map<String, Boolean> getLikedRecipes() {
        return likedRecipes;
    }

    public void setLikedRecipes(Map<String, Boolean> likedRecipes) {
        this.likedRecipes = likedRecipes;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}