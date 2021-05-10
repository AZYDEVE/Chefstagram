package edu.neu.madcourse.numad20f_yang_ylescupidez.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Achievement;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;

public class Repo {
    private Repo() {}

    private static Repo INSTANCE;

    private static final String ACHIEVEMENT_PATH = "achievement";
    private static final String RECIPE_PATH = "recipe";
    private static final String USER_PATH = "users";

    private List<Achievement> achievements = new ArrayList<>();
    private static final MutableLiveData<List<Achievement>> liveAchievements = new MutableLiveData<>();

    private Set<Recipe> userRecipes = new HashSet<>();
    private Set<Recipe> likedRecipes = new HashSet<>();
    private static final MutableLiveData<Set<Recipe>> liveUserRecipes = new MutableLiveData<>();
    private static final MutableLiveData<Set<Recipe>> liveLikedRecipes = new MutableLiveData<>();

    private Set<User> userSearchResults = new HashSet<>();
    private Set<Recipe> recipeSearchResults = new HashSet<>();

    private Recipe recipe;
    private static final MutableLiveData<Recipe> liveRecipe = new MutableLiveData<>();

    private User user;
    private static final MutableLiveData<User> liveUser = new MutableLiveData<>();

    public static Repo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repo();
        }
        return INSTANCE;
    }

    public void postRecipe(Recipe recipe) {
        recipe.setCreatorKey(getUserID());
        DatabaseReference db = getDatabaseReference();
        DatabaseReference recipeRef = db.child(RECIPE_PATH);
        DatabaseReference userRef = db.child(USER_PATH).child(getUserID());
        String recipeKey = recipeRef.push().getKey();
        assert recipeKey != null;
        recipeRef.child(recipeKey).setValue(recipe);
        userRef.child("createdRecipes").child(recipeKey).setValue(true);
    }

    public void achieveAchievement(String achievementKey) {
        DatabaseReference db = getDatabaseReference();
        DatabaseReference achievementRef = db.child(ACHIEVEMENT_PATH);
        String userKey = getUserID();
        achievementRef.child("owners").child(userKey).setValue(true);
    }

    public void likeRecipe(String recipeKey) {
        DatabaseReference db = getDatabaseReference();
        DatabaseReference recipeRef = db.child(RECIPE_PATH).child(recipeKey);
        String userKey = getUserID();
        DatabaseReference userRef = db.child(USER_PATH).child(userKey);
        recipeRef.child("likedByUsers").child(userKey).setValue(true);
        userRef.child("likedRecipes").child(recipeKey).setValue(true);
    }

    public void unlikeRecipe(String recipeKey) {
        DatabaseReference db = getDatabaseReference();
        DatabaseReference recipeRef = db.child(RECIPE_PATH).child(recipeKey);
        String userKey = getUserID();
        DatabaseReference userRef = db.child(USER_PATH).child(userKey);
        recipeRef.child("likedByUsers").child(userKey).removeValue();
        userRef.child("likedRecipes").child(recipeKey).removeValue();
    }

    public MutableLiveData<Recipe> getRecipe(final String recipeKey) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (recipe == null) {
                    recipe = new Recipe();
                }
                loadRecipe(recipeKey);
            }
        }).start();
        liveRecipe.setValue(recipe);
        return liveRecipe;
    }

    public MutableLiveData<User> getLoggedInUser() {
        return getUser(getUserID());
    }

    public MutableLiveData<User> getUser(final String userKey) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (user == null) {
                    user = new User();
                }
                loadUser(userKey);
            }
        }).start();
        liveUser.setValue(user);
        return liveUser;
    }

    public MutableLiveData<List<Achievement>> getAchievements() {
        if (achievements.isEmpty()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    loadAchievements();
                }
            }).start();
        }
        liveAchievements.setValue(achievements);
        return liveAchievements;
    }

    public MutableLiveData<Set<Recipe>> getLikedRecipes() {
        loadLikedRecipes();
        liveLikedRecipes.setValue(likedRecipes);
        return liveLikedRecipes;
    }

    public MutableLiveData<Set<Recipe>> getUserRecipes(String userKey) {
        loadUserRecipes(userKey);
        liveUserRecipes.setValue(userRecipes);
        return liveUserRecipes;
    }

    private void loadUser(final String userKey) {
        final DatabaseReference userRef = getDatabaseReference().child(USER_PATH);
        userRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user == null) {
                    return;
                }
                user.setKey(snapshot.getKey());
                liveUser.postValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadRecipe(final String recipeKey) {
        final DatabaseReference recipeRef = getDatabaseReference().child(RECIPE_PATH);
        final DatabaseReference userRef = getDatabaseReference().child(USER_PATH);
        recipeRef.child(recipeKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recipe = snapshot.getValue(Recipe.class);
                if (recipe == null) {
                    return;
                }
                recipe.setKey(recipeKey);
                String creatorKey = recipe.getCreatorKey();
                userRef.child(creatorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User creator = snapshot.getValue(User.class);
                        creator.setKey(snapshot.getKey());
                        recipe.setCreator(creator);
                        liveRecipe.postValue(recipe);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public MutableLiveData<Set<Recipe>> getMyRecipes() {
        return getUserRecipes(getUserID());
    }

    private void loadLikedRecipes() {

        final DatabaseReference recipeRef = getDatabaseReference().child(RECIPE_PATH);
        final DatabaseReference userRef = getDatabaseReference().child(USER_PATH);
        userRef.child(getUserID()).child("likedRecipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    final String recipeKey = child.getKey();
                    recipeRef.child(recipeKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            recipe = snapshot.getValue(Recipe.class);
                            recipe.setKey(recipeKey);
                            String creatorKey = recipe.getCreatorKey();
                            userRef.child(creatorKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    User creator = snapshot.getValue(User.class);
                                    creator.setKey(snapshot.getKey());
                                    recipe.setCreator(creator);
                                    likedRecipes.add(recipe);
                                    liveLikedRecipes.postValue(likedRecipes);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadUserRecipes(final String userKey) {
        final DatabaseReference recipeRef = getDatabaseReference().child(RECIPE_PATH);
        final DatabaseReference userRef = getDatabaseReference().child(USER_PATH);
        userRef.child(userKey).child("createdRecipes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    final String recipeKey = child.getKey();
                    userRef.child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            final User creator = snapshot.getValue(User.class);
                            creator.setKey(snapshot.getKey());
                            recipeRef.child(recipeKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    recipe = snapshot.getValue(Recipe.class);
                                    recipe.setCreator(creator);
                                    recipe.setKey(recipeKey);
                                    userRecipes.add(recipe);
                                    liveUserRecipes.postValue(userRecipes);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAchievements() {
        DatabaseReference reference = getDatabaseReference();
        Query query = reference.child(ACHIEVEMENT_PATH);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Achievement achievement = child.getValue(Achievement.class);
                    assert achievement != null;
                    achievement.setAchieved(achievement.achievedByUser(getUserID()));
                    achievement.setKey(child.getKey());
                    achievements.add(achievement);
                }
                liveAchievements.postValue(achievements);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private static DatabaseReference getDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    private static String getUserID() {
        return getFirebaseUID();
    }

    private static String getFirebaseUID() {
        return FirebaseAuth.getInstance().getUid();
    }
    
    
}
