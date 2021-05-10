package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;

public class MyViewHolderRecipe extends RecyclerView.ViewHolder {

    ImageView recipeImage;
    TextView recipeName;
    TextView numberOfLikes;
     View view;

    public  MyViewHolderRecipe(@NonNull View itemView) {
        super(itemView);

        recipeImage = itemView.findViewById(R.id.card_img_search);
        recipeName =itemView.findViewById(R.id.card_title_search);
        numberOfLikes = itemView.findViewById(R.id.card_numberOfLikes_search);
        view=itemView;
    }
}
