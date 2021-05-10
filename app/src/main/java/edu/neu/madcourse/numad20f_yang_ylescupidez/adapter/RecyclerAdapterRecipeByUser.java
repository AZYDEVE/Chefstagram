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
        import edu.neu.madcourse.numad20f_yang_ylescupidez.R;
        import edu.neu.madcourse.numad20f_yang_ylescupidez.model.Recipe;

public class RecyclerAdapterRecipeByUser extends RecyclerView.Adapter<RecyclerAdapterRecipeByUser.ViewHolder> {

    private ArrayList<Recipe> recipes;
    private Context mContext;

    public RecyclerAdapterRecipeByUser(ArrayList<Recipe> recipes, Context mContext) {
        this.recipes = recipes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_created_recipe, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(recipes.get(position).getPictureUrl().get(0)).into(holder.image);
        holder.title.setText(recipes.get(position).getRecipeName());
        holder.numberOfLike.setText(recipes.get(position).getNumberOfLikes()+"");
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title;
        TextView numberOfLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.card_recipe_image_byUser);
            title =itemView.findViewById(R.id.car_recipe_title_byUser);
            numberOfLike=itemView.findViewById(R.id.card_numberOfLikes_byUser);
        }
    }
}
