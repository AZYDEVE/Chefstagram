package edu.neu.madcourse.numad20f_yang_ylescupidez.model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Recipe {

    private String recipeName;
    private String recipeDescription;
    public ArrayList<String> pictureUrl;
    private ArrayList<Ingredient> Ingredients;
    private ArrayList<RecipeStep> recipeStep;
    private Map<String, Boolean> likedByUsers = new HashMap<>();
    private String creatorKey;
    @Exclude
    private String key;
    @Exclude
    private User creator;

    public Recipe(String recipeName, String recipeDescription, ArrayList<String> pictureUrl, ArrayList<Ingredient> ingredients, ArrayList<RecipeStep> recipeStep) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.pictureUrl = pictureUrl;
        this.Ingredients = ingredients;
        this.recipeStep = recipeStep;
    }


    public Recipe() {
    }


    public String getRecipeName() {
        return recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public ArrayList<String> getPictureUrl() {
        return pictureUrl;
    }

    public ArrayList<Ingredient> getIngredients() {
        return Ingredients;
    }

    public ArrayList<RecipeStep> getRecipeStep() {
        return recipeStep;
    }

    public int getNumberOfLikes() {
        return likedByUsers.size();
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public void setPictureUrl(ArrayList<String> pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

    public void setRecipeStep(ArrayList<RecipeStep> recipeStep) {
        this.recipeStep = recipeStep;
    }

    public void addLike(String userKey) {
        this.likedByUsers.put(userKey, true);
    }

    public void setCreatorKey(String creatorKey) {
        this.creatorKey = creatorKey;
    }

    public String getCreatorKey() {
        return this.creatorKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return key.equals(recipe.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
