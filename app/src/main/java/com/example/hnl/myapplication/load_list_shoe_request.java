package com.example.hnl.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.item_class.Order;
import com.example.hnl.myapplication.item_class.Request;
import com.example.hnl.myapplication.item_class.Shoe;
import com.example.hnl.myapplication.viewHolder.RequestViewHolder;
import com.example.hnl.myapplication.viewHolder.ShoeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class load_list_shoe_request extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference request;
    RecyclerView recycler_request_detail;
    RecyclerView.LayoutManager layoutManager;
    String key_orders;
    FirebaseRecyclerAdapter<Order,CartViewHolder > adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_list_shoe_request);
        database=FirebaseDatabase.getInstance();
        request=database.getReference("shoe");
        recycler_request_detail=(RecyclerView)findViewById(R.id.recyclerview_shoe_re);
        recycler_request_detail.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recycler_request_detail.setLayoutManager(gridLayoutManager);
        if(getIntent()!=null)
        {
            key_orders=getIntent().getStringExtra("keyOrder");
        }
        if(!key_orders.isEmpty() && key_orders!=null)
        {
            loadRequestDetail(key_orders);
        }

    }

    private void loadRequestDetail(String key_orders) {
        Query query = FirebaseDatabase .getInstance() .getReference("Requests").child(key_orders).child("shoes");
        FirebaseRecyclerOptions<Order> options =
                new FirebaseRecyclerOptions.Builder<Order>()
                        .setQuery(query, Order.class)
                        .build();

        adapter=new FirebaseRecyclerAdapter<Order, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Order model) {
                holder.txtShoesName.setText(model.getProductName());
                holder.txtShoesPrice.setText(model.getPrice());

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cart_item, viewGroup, false);

                return new CartViewHolder(view) {
                };
            }
        };
        recycler_request_detail.setAdapter(adapter);
    }
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtShoesName,txtShoesPrice;

        private ItemClickListener itemClickListener;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtShoesName = (TextView) itemView.findViewById(R.id.txtShoesName);
            txtShoesPrice = (TextView) itemView.findViewById(R.id.txtShoesPrice);
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
}
