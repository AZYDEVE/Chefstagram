package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.SQLOutput;
import java.util.ArrayList;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterRecipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterRecipeByUser;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;

public class ProfileFragmentViewer extends Fragment {


    DatabaseReference database;
    String firebase_UID;
    ArrayList<Recipe> recipesList =new ArrayList<>();;
    RecyclerAdapterRecipeByUser adapter;
    String userId;
    TextView userName, numberOfLikes, bio;
    ImageView profileImage;
    RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();

        userId = bundle.getString("SelectedUserId");


        View profileFragmentViewer = inflater.inflate(R.layout.fragment_profile_viewer, container, false);


        firebase_UID = FirebaseAuth.getInstance().getUid(); // the current user firebase UID
        database = FirebaseDatabase.getInstance().getReference(); // database storage

        userName = profileFragmentViewer.findViewById(R.id.user_name_viewer);
        numberOfLikes = profileFragmentViewer.findViewById(R.id.number_of_likes_viwer);
        bio = profileFragmentViewer.findViewById(R.id.bio_description_viewer);
        profileImage = profileFragmentViewer.findViewById(R.id.profile_image_viewer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(profileFragmentViewer.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView =profileFragmentViewer.findViewById(R.id.recyclerview_recipe_byUse_viewer);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterRecipeByUser(recipesList, profileFragmentViewer.getContext());
        recyclerView.setAdapter(adapter);


        getCurrentUserData();

        return profileFragmentViewer;








    }

    private void getCurrentUserData() {

        DatabaseReference currentUserRef = database.child("users/" + userId);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getUserName());
                bio.setText(user.getBio());

                if (profileImage != null) {
                    Glide.with(getContext())
                            .asDrawable()
                            .load(user.getProfilePicture()).into(profileImage);
                }


                DatabaseReference db = database.child("recipe");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        int numLikes = 0;
                        for (DataSnapshot child : children) {
                            Recipe recipe = child.getValue(Recipe.class);

                            if (recipe.getCreatorKey().equals(userId)) {
                                recipesList.add(recipe);
                                System.out.println(recipe.getPictureUrl());
                                numLikes = numLikes + recipe.getNumberOfLikes();
                            }
                        }

                        if (recipesList.size() != 0) {
                            System.out.println("alex yang heloo");
                            adapter.notifyDataSetChanged();

                        }

                        numberOfLikes.setText(numLikes + "");
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