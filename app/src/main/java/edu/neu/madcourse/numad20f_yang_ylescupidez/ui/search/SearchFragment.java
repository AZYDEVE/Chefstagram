package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.profile.ProfileFragmentViewer;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.RecipeFragment;

public class SearchFragment extends Fragment {

    ArrayList<User> userList;
    RadioButton radioButton;
    RadioGroup radioGroup;
    EditText editTextSearchUser;
    DatabaseReference userRef;
    DatabaseReference recipeRef;
    private FirebaseRecyclerOptions<User> options;
    private FirebaseRecyclerOptions<Recipe> optionsRecipe;
    private FirebaseRecyclerAdapter<User, MyViewHolder> adapter;
    private FirebaseRecyclerAdapter<Recipe, MyViewHolderRecipe> adapterRecipe;

    RecyclerView recyclerViewSearch;
    View searchFragment;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userList = new ArrayList<>();
        searchFragment = inflater.inflate(R.layout.fragment_search, container, false);
        userRef = FirebaseDatabase.getInstance().getReference().child("users");
        recipeRef = FirebaseDatabase.getInstance().getReference().child("recipe");

        radioGroup = searchFragment.findViewById(R.id.radioGroup_search);
        radioButton=searchFragment.findViewById(R.id.radio_recipe);

        searchFragment.findViewById(R.id.radio_recipe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              radioButton = searchFragment.findViewById(R.id.radio_recipe);
              loading();
            }
        });

        searchFragment.findViewById(R.id.radio_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButton = searchFragment.findViewById(R.id.radio_user);
                loading();
            }
        });

        editTextSearchUser = searchFragment.findViewById(R.id.editText_search_user);

        recyclerViewSearch = searchFragment.findViewById(R.id.recyclerview_search);
        recyclerViewSearch.setHasFixedSize(true);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));

        loading();

        editTextSearchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                switch (radioButton.getText().toString()){
                    case "USER":
                        if (s.toString() != null ) {
                            loadUser(s.toString());
                        } else {
                            loadUser("");
                        }
                        break;
                    case "RECIPE":
                        if (s.toString() != null ) {
                            loadRecipe(s.toString());
                        } else {
                            loadRecipe("");
                        }
                        break;
                }

            }
        });


        return searchFragment;
    }

    private void loading(){
        if(radioButton.getText().toString().equals("USER")){
            loadUser("");
        }else{
            loadRecipe("");
        }
    }



    private void loadUser(String searchText) {
        Query query = userRef.orderByChild("userName").startAt(searchText).endAt(searchText + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build();
        adapter = new FirebaseRecyclerAdapter<User, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull final User model) {
                holder.userName.setText(model.getUserName());
                Glide.with(searchFragment).asBitmap().load(model.getProfilePicture()).into(holder.userImage);
                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Bundle bundle = new Bundle();
                        bundle.putString("SelectedUserId", model.getKey());
                        ProfileFragmentViewer profileFragmentViewer = new ProfileFragmentViewer();
                        profileFragmentViewer.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, profileFragmentViewer).commit();

                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_search_users, parent, false);
                return new MyViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerViewSearch.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void loadRecipe(String searchText) {
        Query query = recipeRef.orderByChild("recipeName").startAt(searchText).endAt(searchText + "\uf8ff");

        optionsRecipe = new FirebaseRecyclerOptions.Builder<Recipe>().setQuery(query, Recipe.class).build();
        adapterRecipe = new FirebaseRecyclerAdapter<Recipe, MyViewHolderRecipe>(optionsRecipe) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolderRecipe holder, int position, @NonNull final Recipe model) {
                holder.recipeName.setText(model.getRecipeName());
                holder.numberOfLikes.setText(model.getNumberOfLikes() + "");

                if (model.getPictureUrl().size() >= 0) {
                    Glide.with(searchFragment).asBitmap().load(model.getPictureUrl().get(0)).into(holder.recipeImage);
                }

                holder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO SEAN : you can use below "SelectedRecipe" to get the recipe key for populating data for the RecipeFragment
                        Bundle bundle = new Bundle();
                        bundle.putString("SelectedRecipeId", model.getKey());
                        RecipeFragment recipeFragment = new RecipeFragment();
                        recipeFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, recipeFragment).commit();

                    }
                });
            }

            @NonNull
            @Override
            public MyViewHolderRecipe onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe_search, parent, false);
                return new MyViewHolderRecipe(view);
            }
        };

        adapterRecipe.startListening();
        recyclerViewSearch.setAdapter(adapterRecipe);
        adapterRecipe.notifyDataSetChanged();


    }


}