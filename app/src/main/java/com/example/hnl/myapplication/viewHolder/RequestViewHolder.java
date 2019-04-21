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

import org.w3c.dom.Text;

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtName_Customer;
    public TextView txtPhone_Customer;
    public TextView txtTotal_Price;
    private ItemClickListener itemClickListener;

    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName_Customer = itemView.findViewById(R.id.txt_name_Customer);
        txtPhone_Customer = itemView.findViewById(R.id.txt_phone_Customer);
        txtTotal_Price = itemView.findViewById(R.id.txt_total_price);
        itemView.setOnClickListener(this);

    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
