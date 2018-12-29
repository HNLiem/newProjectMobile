package com.example.hnl.myapplication;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.item_class.Shoe;
import com.example.hnl.myapplication.viewHolder.ShoeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
public class shoes_list extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference shoelist;
    String categoryId="";
    FirebaseRecyclerAdapter<Shoe, ShoeViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoes_list);
        //firebase
        database=FirebaseDatabase.getInstance();
        shoelist=database.getReference("Shoe");
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview_shoes);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(getIntent()!=null)
        {
            categoryId=getIntent().getStringExtra("CategoryId");
        }
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            loadListShoe(categoryId);
        }

    }

    private void loadListShoe(String categoryId) {
        Query query = FirebaseDatabase .getInstance() .getReference("Shoe") .orderByChild("MenuId").equalTo(categoryId);
        FirebaseRecyclerOptions<Shoe> options = new FirebaseRecyclerOptions.Builder<Shoe>().setQuery(query, Shoe.class).build();
        adapter=new FirebaseRecyclerAdapter<Shoe, ShoeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ShoeViewHolder holder, int position, @NonNull Shoe model) {
                holder.shoe_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.shoe_image);



                final Shoe local=model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent shoeDetail=new Intent(shoes_list.this,ShoeDetail.class);
                        shoeDetail.putExtra("ShoeId",adapter.getRef(position).getKey());
                        startActivity(shoeDetail);
                    }
                });
            }

            @NonNull
            @Override
            public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.shoeitem, viewGroup, false);

                return new ShoeViewHolder(view) {
                };
            }
        };
        recyclerView.setAdapter(adapter);
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
}
