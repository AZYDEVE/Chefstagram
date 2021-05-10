package edu.neu.madcourse.numad20f_yang_ylescupidez.ui.search;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.numad20f_yang_ylescupidez.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView userImage;
    TextView userName;
    View view;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        userImage = itemView.findViewById(R.id.profile_image_search_card);
       userName =itemView.findViewById(R.id.userName_search_card);
       view=itemView;


    }
}
