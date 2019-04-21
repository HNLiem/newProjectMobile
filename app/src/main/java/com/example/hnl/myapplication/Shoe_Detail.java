package com.example.hnl.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.hnl.myapplication.Database.Database;
import com.example.hnl.myapplication.fragment.CartFragment;

import com.example.hnl.myapplication.item_class.Order;
import com.example.hnl.myapplication.item_class.Shoe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Shoe_Detail extends AppCompatActivity {
    TextView shoename,name,shoeprice,shoedescription;
    ImageView shoeimage;
    FirebaseDatabase database;
    DatabaseReference shoes;
    String shoeId="";
    FloatingActionButton addCart;
    Shoe Shoes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoe_detail);
        database = FirebaseDatabase.getInstance();
        shoes = database.getReference("Shoe");
        shoename = (TextView) findViewById(R.id.txtNameShoe);
        name = (TextView) findViewById(R.id.txtName);
        shoedescription = (TextView) findViewById(R.id.txtdescription);
        shoeprice = (TextView) findViewById(R.id.txtprice);
        shoeimage = (ImageView) findViewById(R.id.vpImage);
        addCart = (FloatingActionButton) findViewById(R.id.fbaddToCart);

        if (getIntent() != null) {
            shoeId = getIntent().getStringExtra("ShoeId");
        }
        if (!shoeId.isEmpty() && shoeId != null) {
            getDetailShoe(shoeId);
        }

        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        shoeId,
                        Shoes.getName(),
                        Shoes.getPrice(),
                        Shoes.getDiscount()
                ));

                Toast.makeText(Shoe_Detail.this," Added to your Cart ",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDetailShoe(String shoeId) {
        shoes.child(shoeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shoes=dataSnapshot.getValue(Shoe.class);
                Picasso.with(getBaseContext()).load(Shoes.getImage()).into(shoeimage);
                shoeprice.setText(Shoes.getPrice());
                shoename.setText(Shoes.getName());
                shoedescription.setText(Shoes.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}
