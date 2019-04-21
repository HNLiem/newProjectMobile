package com.example.hnl.myapplication.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hnl.myapplication.Common.Common;
import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.interfaces.ItemClickListener;

public abstract class ShoeViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,View.OnCreateContextMenuListener {
    public TextView shoe_name;
    public ImageView shoe_image;
    private ItemClickListener itemClickListener;
    Common common;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ShoeViewHolder(@NonNull View itemView) {
        super(itemView);
        shoe_name=(TextView)itemView.findViewById(R.id.shoe_name);
        shoe_image=(ImageView)itemView.findViewById(R.id.shoe_image);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action:");
        menu.add(0,0,getAdapterPosition(),common.UPDATE);
        menu.add(0,1,getAdapterPosition(),common.DELETE);
    }
}