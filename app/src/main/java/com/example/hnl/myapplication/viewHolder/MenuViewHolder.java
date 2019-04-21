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


public class MenuViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener{
    Common common;
    public TextView txtMenuName;
    public ImageView imageView;
    private ItemClickListener itemClickListener;
    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        txtMenuName=(TextView) itemView.findViewById(R.id.txt_brand_name);
        imageView = (ImageView) itemView.findViewById(R.id.img_brand_demo);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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
