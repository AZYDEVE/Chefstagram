package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.recipes.createRecipe;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Ingredient;


public class CreateIngredientFragment extends Fragment implements View.OnClickListener {
    LinearLayout layoutList;
    Button buttonAdd, buttonNext;
    EditText recipeName, recipeDescription; //will forward to the createIngredientsStepFragment


    List<String> amount = new ArrayList<>();
    ArrayList<Ingredient> ingredientsList = new ArrayList<>(); //will forward to the createIngredientsStepFragment

    ArrayList<Uri> recipePictures; // picture pass from fragment - createRecipeFragment, will forward to the createIngredientsStepFragment


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View createIngredientFragment = inflater.inflate(R.layout.fragment_create_ingredient, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            recipePictures = bundle.getParcelableArrayList("picture");  // picture pass from fragment - createRecipeFragment,
        }

        recipeName = createIngredientFragment.findViewById(R.id.recipe_name);
        recipeDescription = createIngredientFragment.findViewById(R.id.recipe_description);
        layoutList = createIngredientFragment.findViewById(R.id.layout_list);
        buttonAdd = createIngredientFragment.findViewById(R.id.button_add);
        buttonNext = createIngredientFragment.findViewById(R.id.button_next);

        buttonAdd.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        amount.add("oz");
        amount.add("cup");
        amount.add("tsp");
        amount.add("pcs");
        amount.add("qt");
        amount.add("gl");


        return createIngredientFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add:
                addView();
                break;
            case R.id.button_next:
                if (checkIfValidAndRead()) { // if this function returns true
                    /*  To do: this is where to going to put data to the next fragment */
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("picture", recipePictures);
                    bundle.putString("recipeName", recipeName.getText().toString());
                    bundle.putString("recipeDescription", recipeDescription.getText().toString());
                    bundle.putSerializable("ingredientList", ingredientsList);
                    CreateRecipeStepFragment createRecipeStepFragment = new CreateRecipeStepFragment();
                    createRecipeStepFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, createRecipeStepFragment).addToBackStack(null).commit();

                }
                ;
                break;
        }

    }

    // store values in the ingredient list in arrayList
    private boolean checkIfValidAndRead() {
        ingredientsList.clear();
        boolean result = true;

        for (int i = 0; i < layoutList.getChildCount(); i++) {
            View eachIngredient = layoutList.getChildAt(i);

            EditText editTextIngredientName = eachIngredient.findViewById(R.id.edit_ingredient_name);
            EditText editTextIngredientAmount = eachIngredient.findViewById(R.id.edit_ingredient_amount);
            AppCompatSpinner spinner = eachIngredient.findViewById(R.id.spinner);
            Ingredient ingredient = new Ingredient();

            if (!editTextIngredientName.getText().toString().equals("")) {
                ingredient.setIngredient(editTextIngredientName.getText().toString());
            } else {
                result = false;
                break;
            }

            if (!editTextIngredientAmount.getText().toString().equals("")) {
                ingredient.setSize(editTextIngredientAmount.getText().toString());
            } else {
                result = false;
                break;
            }

            ingredient.setUnit(amount.get(spinner.getSelectedItemPosition()));


            ingredientsList.add(ingredient); // if not error, put the ingredient to the arrayList
        }

        if (ingredientsList.size() == 0) {
            result = false;
            Toast.makeText(getContext(), "Please add Ingredients first", Toast.LENGTH_LONG).show();
        } else if (!result) {
            Toast.makeText(getContext(), "Please don't leave fields empty", Toast.LENGTH_LONG).show();
        }

        if (recipeName.getText().toString().equals("") || recipeDescription.getText().toString().equals("")) {
            result = false;
            Toast.makeText(getContext(), "recipe name or recipe description is empty", Toast.LENGTH_LONG).show();
        }


        return result;
    }

    private void addView() {

        final View ingredientView = getLayoutInflater().inflate(R.layout.row_add, null, false);
        EditText editTextIngredientName = ingredientView.findViewById(R.id.edit_ingredient_name);
        EditText editTextIngredientAmount = ingredientView.findViewById(R.id.edit_ingredient_amount);
        AppCompatSpinner spinner = ingredientView.findViewById(R.id.spinner);
        ImageView imageClose = ingredientView.findViewById(R.id.image_remove);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, amount);
        spinner.setAdapter(arrayAdapter);

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(ingredientView);
            }
        });
        layoutList.addView(ingredientView);

    }

    private void removeView(View view) {
        layoutList.removeView(view);

    }
}