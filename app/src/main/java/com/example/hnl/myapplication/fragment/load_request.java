package com.example.hnl.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.item_class.Category;
import com.example.hnl.myapplication.item_class.Order;
import com.example.hnl.myapplication.item_class.Request;
import com.example.hnl.myapplication.item_class.Shoe;
import com.example.hnl.myapplication.load_list_shoe_request;
import com.example.hnl.myapplication.shoes_list;
import com.example.hnl.myapplication.viewHolder.MenuViewHolder;
import com.example.hnl.myapplication.viewHolder.RequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class load_request extends Fragment {


    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recycler_request_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Request, RequestViewHolder> adapter;


    public load_request() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot =  inflater.inflate(R.layout.fragment_load_request, container, false);
        database=FirebaseDatabase.getInstance();
        category=database.getReference("Requests");
        recycler_request_menu=(RecyclerView)viewRoot.findViewById(R.id.requests_list);
        recycler_request_menu.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recycler_request_menu.setLayoutManager(gridLayoutManager);
        loadRequest();
        // Inflate the layout for this fragment
        return viewRoot;
    }

    private void loadRequest() {
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Requests");

        FirebaseRecyclerOptions<Request> options =
                new FirebaseRecyclerOptions.Builder<Request>()
                        .setQuery(query, Request.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Request, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull final Request model) {
                holder.txtName_Customer.setText(model.getName());
                holder.txtTotal_Price.setText(model.getTotal());
                holder.txtPhone_Customer.setText(model.getPhone());
                final Request clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent newintent = new Intent(getActivity(),load_list_shoe_request.class);

                        newintent.putExtra("keyOrder",model.getKey_order());
                        startActivity(newintent);
                        Toast.makeText(getActivity(),"Da bam",Toast.LENGTH_SHORT).show();


                    }
                });
            }

            @Override
            public RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_request, parent, false);

                return new RequestViewHolder(view);
            }



        };

        adapter.getItemCount();
        recycler_request_menu.setAdapter(adapter);
    }
    @Override
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
