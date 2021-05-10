package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.achievements.AchievementsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String ARG_RECIPE_ID = "recipeKey";

    // TODO: Rename and change types of parameters
    private String recipeKey;

    private RecipeViewModel model;

    private TextView name;
    private TextView description;
    private TextView creatorName;
    private ImageView creatorPfp;
    private Button likeButton;

    private boolean likeStatus = false;

    public RecipeInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static RecipeInfoFragment newInstance(String description) {
        RecipeInfoFragment fragment = new RecipeInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            recipeKey = getArguments().getString(ARG_RECIPE_ID);
        }
        model = ViewModelProviders.of(this).get(RecipeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_recipe_info, container, false);
        name = root.findViewById(R.id.recipe_title);
        description = root.findViewById(R.id.recipe_description);
        creatorName = root.findViewById(R.id.recipe_creator);
        creatorPfp = root.findViewById(R.id.recipe_creator_pfp);
        likeButton = root.findViewById(R.id.like_button);

        model.getRecipe(recipeKey).observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(Recipe recipe) {
                if (recipe == null) {
                    return;
                }
                name.setText(recipe.getRecipeName());
                description.setText(recipe.getRecipeDescription());
                User creator = recipe.getCreator();
                creatorName.setText(creator.getUserName());
                Glide.with(getContext())
                        .asDrawable()
                        .load(creator.getProfilePicture()).into(creatorPfp);
                // TODO: RecyclerViews
            }
        });
        // TODO: Like / Unlike
        View.OnClickListener likeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeStatus) {
                    model.unlikeRecipe(recipeKey);
                    likeButton.setText(R.string.hollow_heart);
                } else {
                    model.likeRecipe(recipeKey);
                    likeButton.setText(R.string.filled_heart);
                }
                likeStatus = !likeStatus;
            }
        };
        likeButton.setOnClickListener(likeButtonListener);
        model.getLoggedInUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user == null) {
                    return;
                }
                likeStatus = user.getLikedRecipes().containsKey(recipeKey);
                if (likeStatus) {
                    likeButton.setText(R.string.filled_heart);
                } else {
                    likeButton.setText(R.string.hollow_heart);
                }
            }
        });
        return root;
    }


}