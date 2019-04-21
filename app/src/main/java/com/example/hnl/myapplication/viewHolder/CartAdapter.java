package com.example.hnl.myapplication.viewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.hnl.myapplication.interfaces.ItemClickListener;

import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.item_class.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtShoesName,txtShoesPrice;

    private ItemClickListener itemClickListener;


    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtShoesName = (TextView) itemView.findViewById(R.id.txtShoesName);
        txtShoesPrice = (TextView) itemView.findViewById(R.id.txtShoesPrice);
    }

    @Override
    public void onClick(View v) {

    }
}

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> listData;
    private Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item,viewGroup,false);


        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i) {
        cartViewHolder.txtShoesName.setText(listData.get(i).getProductName());
        cartViewHolder.txtShoesPrice.setText(listData.get(i).getPrice());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}


