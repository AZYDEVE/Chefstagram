package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Set;

import edu.neu.madcourse.numad20f_yang_ylescupidez.db.Repo;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;

public class RecipeViewModel extends ViewModel {
    private LiveData<Set<Recipe>> likedRecipes;
    private LiveData<Set<Recipe>> userRecipes;
    private LiveData<Recipe> recipe;
    private LiveData<User> user;

    public LiveData<Set<Recipe>> getLikedRecipes() {
        if (likedRecipes == null) {
            likedRecipes = Repo.getInstance().getLikedRecipes();
        }
        return likedRecipes;
    }

    public LiveData<Set<Recipe>> getMyRecipes() {
        if (userRecipes == null) {
            // userRecipes = Repo.getInstance().getMyRecipes();
            userRecipes = Repo.getInstance().getUserRecipes("7vpyzmzXqYQ79aWx7F8vePKGZv03");
        }
        return userRecipes;
    }

    public LiveData<Set<Recipe>> getUserRecipes(String userKey) {
        if (userRecipes == null) {
            userRecipes = Repo.getInstance().getUserRecipes(userKey);
        }
        return userRecipes;
    }

    public LiveData<Recipe> getRecipe(String recipeKey) {
        if (recipe == null) {
            recipe = Repo.getInstance().getRecipe(recipeKey);
        }
        return recipe;
    }

    public LiveData<User> getLoggedInUser() {
        if (user == null) {
            user = Repo.getInstance().getLoggedInUser();
        }
        return user;
    }

    public void likeRecipe(String recipeKey) {
        Repo.getInstance().likeRecipe(recipeKey);
    }

    public void unlikeRecipe(String recipeKey) {
        Repo.getInstance().unlikeRecipe(recipeKey);
    }
}
