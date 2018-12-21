package com.example.hnl.myapplication.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.interfaces.ItemClickListener;

public abstract class ShoeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView shoe_name;
    public ImageView shoe_image;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ShoeViewHolder(@NonNull View itemView) {
        super(itemView);
        shoe_name=(TextView)itemView.findViewById(R.id.shoe_name);
        shoe_image=(ImageView)itemView.findViewById(R.id.shoe_image);
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}