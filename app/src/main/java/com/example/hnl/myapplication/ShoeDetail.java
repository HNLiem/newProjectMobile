package com.example.hnl.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hnl.myapplication.item_class.Shoe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShoeDetail extends AppCompatActivity {

    TextView shoename,name,shoeprice,shoedescription;
    ImageView shoeimage;
    FirebaseDatabase database;
    DatabaseReference shoes;
    String shoeId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database=FirebaseDatabase.getInstance();
        shoes=database.getReference("Shoe");
        shoename=(TextView)findViewById(R.id.txtNameShoe);
        name=(TextView)findViewById(R.id.txtName);
        shoedescription=(TextView)findViewById(R.id.txtdescription);
        shoeprice=(TextView)findViewById(R.id.txtprice);
        shoeimage= (ImageView) findViewById(R.id.simage);
        if(getIntent()!=null)
        {
            shoeId=getIntent().getStringExtra("ShoeId");
        }
        if(!shoeId.isEmpty() && shoeId!=null)
        {
            getDetailShoe(shoeId);
        }

    }

    private void getDetailShoe(String shoeId)
    {
        shoes.child(shoeId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Shoe shoe=dataSnapshot.getValue(Shoe.class);
                Picasso.with(getBaseContext()).load(shoe.getImage()).into(shoeimage);
                shoeprice.setText(shoe.getPrice());
                shoename.setText(shoe.getName());
                shoedescription.setText(shoe.getDescription());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
