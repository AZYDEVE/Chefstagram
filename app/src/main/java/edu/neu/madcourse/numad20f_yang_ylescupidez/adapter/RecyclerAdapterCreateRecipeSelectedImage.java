package edu.neu.madcourse.numad20f_yang_ylescupidez.adapter;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import edu.neu.madcourse.numad20f_yang_ylescupidez.R;


public class RecyclerAdapterCreateRecipeSelectedImage extends RecyclerView.Adapter<RecyclerAdapterCreateRecipeSelectedImage.ViewHolder> {

    private ArrayList<Uri> images;
    private Context mContext;

    public RecyclerAdapterCreateRecipeSelectedImage(ArrayList < Uri> images, Context mContext) {
        this.images =images;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerAdapterCreateRecipeSelectedImage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_create_recipe, parent,false);
        return new RecyclerAdapterCreateRecipeSelectedImage.ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapterCreateRecipeSelectedImage.ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(images.get(position)).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.cardView_image);

        }
    }

}
