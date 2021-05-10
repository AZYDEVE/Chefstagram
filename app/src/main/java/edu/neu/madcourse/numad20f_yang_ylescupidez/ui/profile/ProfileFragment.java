package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterAchievementIcon;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterRecipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterRecipeByUser;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;

import static android.content.ContentValues.TAG;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    StorageReference storageReference;
    DatabaseReference database;
    String firebase_UID;
    ArrayList<Recipe> recipesList;
    RecyclerAdapterRecipeByUser adapter;
    RecyclerAdapterAchievementIcon recyclerAdapterAchievementIcon;
    TextView userName, numberOfLikes, bio;
    EditText userName_et, bio_et; // for change profile info
    Button profileChange, saveProfileChange;
    ImageView camera, gallery, profileImage;
    String currentPhotoPath;
    Uri contentUri;


    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View dashboard_fragment = inflater.inflate(R.layout.fragment_profile, null, false);

        firebase_UID = FirebaseAuth.getInstance().getUid(); // the current user firebase UID
        storageReference = FirebaseStorage.getInstance().getReference(); // image storage
        database = FirebaseDatabase.getInstance().getReference(); // database storage
        recipesList = new ArrayList<>();

        userName = dashboard_fragment.findViewById(R.id.user_name);
        numberOfLikes = dashboard_fragment.findViewById(R.id.number_of_likes);
        bio = dashboard_fragment.findViewById(R.id.bio_description);
        userName_et = dashboard_fragment.findViewById(R.id.userName_editText);
        bio_et = dashboard_fragment.findViewById(R.id.bio_description_editText);
        profileChange = dashboard_fragment.findViewById(R.id.button_change_profile);
        saveProfileChange = dashboard_fragment.findViewById(R.id.button_save_profile);
        camera = dashboard_fragment.findViewById(R.id.camera_change_profile_pic);
        gallery = dashboard_fragment.findViewById(R.id.gallery_change_profile_pic);
        profileImage = dashboard_fragment.findViewById(R.id.profile_image);

        profileChange.setOnClickListener(this);
        saveProfileChange.setOnClickListener(this);
        camera.setOnClickListener(this);
        gallery.setOnClickListener(this);

        getCurrentUserData();

        LinearLayoutManager layoutManager = new LinearLayoutManager(dashboard_fragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = dashboard_fragment.findViewById(R.id.recyclerview_recipe_byUser);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapterRecipeByUser(recipesList, dashboard_fragment.getContext());
        recyclerView.setAdapter(adapter);

        ArrayList<String> sampleListIcon = new ArrayList<>();
        sampleListIcon.add("https://icons.iconarchive.com/icons/seanau/fresh-web/256/Badge-icon.png");
        sampleListIcon.add("https://icons.iconarchive.com/icons/seanau/fresh-web/256/Popcorn-icon.png");

        LinearLayoutManager layoutManagerAchievementIcon = new LinearLayoutManager(dashboard_fragment.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerViewAchievement = dashboard_fragment.findViewById(R.id.recyclerview_badge);
        recyclerViewAchievement.setLayoutManager(layoutManagerAchievementIcon);
        recyclerAdapterAchievementIcon = new RecyclerAdapterAchievementIcon(sampleListIcon, getContext());
        recyclerViewAchievement.setAdapter(recyclerAdapterAchievementIcon);




        return dashboard_fragment;

    }

    private void getCurrentUserData() {

        DatabaseReference currentUserRef = database.child("users/" + firebase_UID);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getUserName());
                userName.setVisibility(View.VISIBLE);
                bio.setText(user.getBio());

                if (profileImage != null) {
                    Glide.with(getContext())
                            .asDrawable()
                            .load(user.getProfilePicture()).into(profileImage);
                }
                userName.setVisibility(View.VISIBLE);
                profileImage.setVisibility(View.VISIBLE);
                profileChange.setVisibility(View.VISIBLE);



                DatabaseReference db = database.child("recipe");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Iterable<DataSnapshot> children = snapshot.getChildren();
                        int numLikes = 0;
                        for (DataSnapshot child : children) {
                            Recipe recipe = child.getValue(Recipe.class);

                            if (recipe.getCreatorKey().equals(firebase_UID)) {
                                recipesList.add(recipe);
                                System.out.println(recipe.getPictureUrl());
                                numLikes = numLikes + recipe.getNumberOfLikes();
                            }
                        }

                        if (recipesList.size() != 0) {

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_change_profile:
                changeProfile();
                break;
            case R.id.button_save_profile:
                saveProfileChanges();
                break;
            case R.id.camera_change_profile_pic:
                askCameraPermission();
                break;
            case R.id.gallery_change_profile_pic:
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
                break;


        }

    }

    private void changeProfile() {
        pastTextFromTextViewToEditText();
        saveProfileChange.setVisibility(View.VISIBLE);
        camera.setVisibility(View.VISIBLE);
        gallery.setVisibility(View.VISIBLE);
        userName_et.setVisibility(View.VISIBLE);
        bio_et.setVisibility(View.VISIBLE);

        profileChange.setVisibility(View.GONE);
        userName.setVisibility(View.INVISIBLE);
        bio.setVisibility(View.INVISIBLE);

    }


    private void saveProfileChanges() {

        pastTextFromEditTextToTextView();
        profileChange.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);
        bio.setVisibility(View.VISIBLE);

        saveProfileChange.setVisibility(View.GONE);
        camera.setVisibility(View.GONE);
        gallery.setVisibility(View.GONE);
        userName_et.setVisibility(View.GONE);
        bio_et.setVisibility(View.GONE);

        if (contentUri != null && !contentUri.equals(Uri.EMPTY)) {
            uploadProfileChangesToDB();
        }else{
            uploadUserNameAndBioOnly();
        }
    }


    private void pastTextFromTextViewToEditText() {
        userName_et.setText(userName.getText().toString());
        bio_et.setText(bio.getText().toString());


    }

    private void pastTextFromEditTextToTextView() {
        userName.setText(userName_et.getText().toString());
        bio.setText(bio_et.getText().toString());
    }

    private void uploadProfileChangesToDB() {

        DatabaseReference currentUserRef = database.child("users/" + firebase_UID);
        currentUserRef.child("bio").setValue(bio_et.getText().toString());
        currentUserRef.child("userName").setValue(userName_et.getText().toString());
        uploadProfileImage();

    }

    private void uploadUserNameAndBioOnly () {
        DatabaseReference currentUserRef = database.child("users/" + firebase_UID);
        currentUserRef.child("bio").setValue(bio_et.getText().toString());
        currentUserRef.child("userName").setValue(userName_et.getText().toString());
    }

    private void uploadProfileImage() {
        final StorageReference profilePicRef = storageReference.child(firebase_UID + "/profileIamge");
        profilePicRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        database.child("users").child(firebase_UID).child("profilePicture").setValue(uri.toString());

                    }
                });
            }
        });
    }


    // GET PERMISSION FOR ACCESSING THE CAMERA
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "edu.neu.madcourse.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    // CREATE FILE NAME
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override   // TO SAVE THE PICTURE
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);

                Log.d(TAG, "ABsolute url of image is " + Uri.fromFile(f));

                contentUri = Uri.fromFile(f);
                profileImage.setImageURI(contentUri);


            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
        /*    Bitmap image = (Bitmap) data.getExtras().get("data");
            picture.setImageBitmap(image);*/
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                profileImage.setImageURI(contentUri);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(getContext(), "Camera permission is required to use camera", Toast.LENGTH_LONG).show();
            }
        }
    }


}