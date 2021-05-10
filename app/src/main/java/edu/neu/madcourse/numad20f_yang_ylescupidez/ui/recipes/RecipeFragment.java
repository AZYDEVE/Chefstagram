package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Set;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.adapter.RecyclerAdapterRecipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.createRecipe.CreateRecipeFragment;

public class RecipeFragment extends Fragment {
    public RecipeFragment() {}

    private RecyclerAdapterRecipe likedRAdapter;
    private RecyclerAdapterRecipe createdRAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recipe, container, false);
        RecipeViewModel model = ViewModelProviders.of(this).get(RecipeViewModel.class);

        RecyclerAdapterRecipe.RecipeCardClickListener recipeClickListener = new RecyclerAdapterRecipe.RecipeCardClickListener() {
            @Override
            public void onRecipeCardClick(Recipe recipe) {
                onRecipeClick(recipe);
            }
        };

        model.getLikedRecipes().observe(getViewLifecycleOwner(), new Observer<Set<Recipe>>() {
            @Override
            public void onChanged(Set<Recipe> recipes) {
                likedRAdapter.notifyDataSetChanged();
            }
        });

        model.getMyRecipes().observe(getViewLifecycleOwner(), new Observer<Set<Recipe>>() {
            @Override
            public void onChanged(Set<Recipe> recipes) {
                createdRAdapter.notifyDataSetChanged();
            }
        });

        RecyclerView likedRecyclerView = root.findViewById(R.id.liked_recipes);
        likedRecyclerView.setHasFixedSize(true);
        likedRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        likedRAdapter = new RecyclerAdapterRecipe(model.getLikedRecipes().getValue(), root.getContext());
        likedRAdapter.setOnClickListener(recipeClickListener);
        likedRecyclerView.setAdapter(likedRAdapter);

        RecyclerView createdRecyclerView = root.findViewById(R.id.my_recipes);
        createdRecyclerView.setHasFixedSize(true);
        createdRecyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false));
        createdRAdapter = new RecyclerAdapterRecipe(model.getMyRecipes().getValue(), root.getContext());
        createdRAdapter.setOnClickListener(recipeClickListener);
        createdRecyclerView.setAdapter(createdRAdapter);

        Button goCreateRecipe = root.findViewById(R.id.go_create_recipe);
        goCreateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateRecipeFragment createRecipeFragment = new CreateRecipeFragment();

                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment,createRecipeFragment).addToBackStack(null).commit();
            }
        });
        return root;
    }

    private void onRecipeClick(Recipe recipe) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString(RecipeInfoFragment.ARG_RECIPE_ID, recipe.getKey());
        Fragment fragment = new RecipeInfoFragment();
        fragment.setArguments(args);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
