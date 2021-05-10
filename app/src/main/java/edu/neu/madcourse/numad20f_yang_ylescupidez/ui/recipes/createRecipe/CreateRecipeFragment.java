package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.createRecipe;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterCreateRecipeSelectedImage;

import static android.content.ContentValues.TAG;

public class CreateRecipeFragment extends Fragment {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int GALLERY_REQUEST_CODE = 105;

    ImageView picture;
    Button cameraBtn, galleryBtn, nextBtn;
    String currentPhotoPath;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerAdapterCreateRecipeSelectedImage recyclerAdapterCreateRecipeSelectedImage;

    StorageReference storageReference;


    ArrayList<Uri> listForRecycView = new ArrayList<>(); //for firebase the uri of the image selected from gallery or taken by phone camera

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View createRecipe_fragment = inflater.inflate(R.layout.fragment_create_recipe, container, false);

        cameraBtn = createRecipe_fragment.findViewById(R.id.btn_camera);
        galleryBtn = createRecipe_fragment.findViewById(R.id.btn_gallary);
        nextBtn = createRecipe_fragment.findViewById(R.id.btn_next_create_recipeName);
        recyclerView = createRecipe_fragment.findViewById(R.id.recylerView_recipe_image);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerAdapterCreateRecipeSelectedImage = new RecyclerAdapterCreateRecipeSelectedImage(listForRecycView, getContext());
        recyclerView.setAdapter(recyclerAdapterCreateRecipeSelectedImage);
        recyclerView.setLayoutManager(layoutManager);

                // Firebase storage reference
        storageReference = FirebaseStorage.getInstance().getReference();

        //if camera button is clicked
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCameraPermission();
            }
        });

        //if gallery button is clicked
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("picture",listForRecycView); // Send the taken pictures to next fragment
                CreateIngredientFragment createIngredientFragment = new CreateIngredientFragment();
                createIngredientFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,createIngredientFragment).addToBackStack(null).commit();
            }
        });


        return createRecipe_fragment;
    }

    // GET PERMISSION FOR ACCESSING THE CAMERA
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            dispatchTakePictureIntent();
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

    // OPEN PHONE CAMERA APP
    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override   // TO SAVE THE PICTURE
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);

                Log.d(TAG, "ABsolute url of image is " + Uri.fromFile(f));

                Uri contentUri = Uri.fromFile(f);
                listForRecycView.add(contentUri); // put photo taken by the camera for recycleview and next fragment
                recyclerAdapterCreateRecipeSelectedImage.notifyDataSetChanged();

//                uploadImageToFirebase(f.getName(),contentUri);

            }

        }

        if (requestCode == GALLERY_REQUEST_CODE) {
        /*    Bitmap image = (Bitmap) data.getExtras().get("data");
            picture.setImageBitmap(image);*/
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyymmdd_HHmmss").format(new Date());
                String imageFileName = "jpeg_" + timeStamp + "." + getFileExt(contentUri);
                Log.d(TAG, "ABsolute url of image is " + imageFileName);
                /* picture.setImageURI(contentUri);*/
                listForRecycView.add(contentUri); // put selected photo for recyclerview and next fragment
                recyclerAdapterCreateRecipeSelectedImage.notifyDataSetChanged();

//                uploadImageToFirebase(imageFileName,contentUri);

            }

        }
    }

    // upload to firebase storage
    private void uploadImageToFirebase(String name, Uri contentUri) {
//        final StorageReference image = storageReference.child("images/" + name);
        final StorageReference image = storageReference.child("images/" );
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "upload uri is " + uri.toString());
                        Toast.makeText(getContext(), "upload successfully", Toast.LENGTH_LONG).show();
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

    private String getFileExt(Uri contentUri) {
        ContentResolver c = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
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
}