package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.createRecipe;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.db.Repo;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Ingredient;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.RecipeStep;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.profile.ProfileFragment;

public class CreateRecipeStepFragment extends Fragment implements View.OnClickListener {
    LinearLayout layoutListStep;
    Button buttonAdd, buttonCreateRecipePost;
    ProgressBar progressBar;

    ArrayList<RecipeStep> stepsList = new ArrayList<>();

    ArrayList<Uri> recipePictures; // for upload to firebase storage
    ArrayList<String> pictureURLStr; // for upload to realtime database
    String recipeName;
    String recipeDescription;
    ArrayList<Ingredient> ingredientList;
    Recipe recipe;

    User currentUser;
    StorageReference storageReference;
    DatabaseReference database;
    String firebase_UID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View createRecipeStepFragment = inflater.inflate(R.layout.fragment_create_recipe_step, container, false);

        Bundle bundle = this.getArguments();
        recipePictures = bundle.getParcelableArrayList("picture");
        recipeName = bundle.getString("recipeName");
        recipeDescription = bundle.getString("recipeDescription");
        ingredientList = (ArrayList<Ingredient>) bundle.getSerializable("ingredientList");

        firebase_UID = FirebaseAuth.getInstance().getUid(); // the current user firebase UID
        storageReference = FirebaseStorage.getInstance().getReference(); // image storage
        database = FirebaseDatabase.getInstance().getReference(); // database storage

//        getUserName(); // get current usersinfo

        pictureURLStr = new ArrayList<>();

        layoutListStep = createRecipeStepFragment.findViewById(R.id.layout_list_steps);
        buttonAdd = createRecipeStepFragment.findViewById(R.id.button_add_steps);
        buttonCreateRecipePost = createRecipeStepFragment.findViewById(R.id.button_submit_step);
        progressBar = createRecipeStepFragment.findViewById(R.id.progress_recipe_step);

        buttonAdd.setOnClickListener(this);
        buttonCreateRecipePost.setOnClickListener(this);


        return createRecipeStepFragment;
    }
//    // get currentUser
//    private void getUserName() {
//        DatabaseReference userInfoPath = database.child("users/" + firebase_UID);
//        userInfoPath.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                currentUser = snapshot.getValue(User.class);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_steps:
                addView();
                break;
            case R.id.button_submit_step:
                if (checkIfValidAndRead()) {
                    /*  To do: this is where to going to put data to the next fragment */
                    // create the recipe obj to be uploaded to firebase database
                    recipe = new Recipe(recipeName, recipeDescription, pictureURLStr, ingredientList, stepsList);
                    UploadRecipe();

                }
                ;
                break;
        }
    }

    // 1. first upload the pictures to firebase storage
    // 2. get the picture access url from firestore
    // 3. create a arrayList with the Url
    // 4. create the recipe object
    // 5. send the recipe obj to firebase realtime database
    private void UploadRecipe() {

        for (Uri uri : recipePictures) {
            /* CompletableFuture.supplyAsync()*/
            uploadImageToFirebase(uri);
        }


    }

    private void uploadImageToFirebase(Uri contentUri) {
//        final StorageReference image = storageReference.child("images/" + name);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        final StorageReference image = storageReference.child(firebase_UID + "/" + recipeName + "/" + contentUri.getLastPathSegment());
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        recipe.getPictureUrl().add(uri.toString());

                        // if the number of url receive from firebase storage == the url in recipePictures
                        // then upload the recipe obj to firebase database
                        if (recipe.getPictureUrl().size() == recipePictures.size()) {
                            System.out.println("success");
//                            DatabaseReference recipeObj = database.child("recipe/" + firebase_UID + "/" + recipeName);
//                            recipeObj.setValue(recipe);

                            Repo.getInstance().postRecipe(recipe);
                            Toast.makeText(getContext(), " your recipe is uploaded successfully", Toast.LENGTH_LONG).show();
                            ProfileFragment profileFragment = new ProfileFragment();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFragment).commit();
                        }

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_LONG).show();

            }
        });
    }


    private void addView() {

        final View recipeStepsView = getLayoutInflater().inflate(R.layout.row_add_steps, null, false);
        EditText editTextIngredientName = recipeStepsView.findViewById(R.id.edit_step_description);
        ImageView imageClose = recipeStepsView.findViewById(R.id.image_remove_step);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(recipeStepsView);
            }
        });
        layoutListStep.addView(recipeStepsView);

    }

    private void removeView(View view) {
        layoutListStep.removeView(view);

    }

    private boolean checkIfValidAndRead() {
        stepsList.clear();
        boolean result = true;

        for (int i = 0; i < layoutListStep.getChildCount(); i++) {
            View eachStep = layoutListStep.getChildAt(i);

            EditText editTextStep = eachStep.findViewById(R.id.edit_step_description);

            RecipeStep step = new RecipeStep();

            if (!editTextStep.getText().toString().equals("")) {
                step.setInstructions(editTextStep.getText().toString());
            } else {
                result = false;
                break;
            }


            stepsList.add(step); // if not error, put the ingredient to the arrayList
        }

        if (stepsList.size() == 0) {
            result = false;
            Toast.makeText(getContext(), "Please add cooking step first", Toast.LENGTH_LONG).show();
        } else if (!result) {
            Toast.makeText(getContext(), "Please don't leave fields empty", Toast.LENGTH_LONG).show();
        }
        return result;
    }

}