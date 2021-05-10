package edu.neu.madcourse.numad20f_yang_ylescupidez.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;
import edu.neu.madcourse.numad20f_yang_ylescupidez.model.User;

public class RecyclerAdapterRecipe extends RecyclerView.Adapter<RecyclerAdapterRecipe.ViewHolder> {

    public static interface RecipeCardClickListener {
        void onRecipeCardClick(Recipe recipe);
    }

    private List<Recipe> recipes;
    private Context mContext;
    private RecipeCardClickListener listener;

    public RecyclerAdapterRecipe(Collection<Recipe> recipes, Context mContext) {
        this.recipes = new ArrayList<>();
        this.recipes.addAll(recipes);
        this.mContext = mContext;
    }

    public void setOnClickListener(RecipeCardClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_recipe, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe currentRecipe = recipes.get(position);
        User recipeCreator = currentRecipe.getCreator();
        Glide.with(mContext)
                .asBitmap()
                .load(currentRecipe.getPictureUrl().get(0)).into(holder.image);
        holder.title.setText(currentRecipe.getRecipeName());
        holder.numberOfLike.setText(currentRecipe.getNumberOfLikes()+"");
        String profileImage = recipeCreator.getProfilePicture();
        if (profileImage != null && !profileImage.isEmpty()) {
            Glide.with(mContext)
                    .asBitmap()
                    .load(profileImage).into(holder.profileImage);
        }
        holder.creator.setText(recipeCreator.getUserName());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView profileImage;
        TextView creator;
        TextView title;
        TextView numberOfLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.card_recipe_image);
            title =itemView.findViewById(R.id.card_recipe_title);
            numberOfLike=itemView.findViewById(R.id.card_numberOfLikes);
            profileImage=itemView.findViewById(R.id.recipe_card_creator_pic);
            creator=itemView.findViewById(R.id.card_recipe_creator);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getLayoutPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onRecipeCardClick(recipes.get(position));
                        }
                    }
                }
            });
        }
    }
}