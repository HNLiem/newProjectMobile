package com.example.hnl.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hnl.myapplication.Common.Common;
import com.example.hnl.myapplication.item_class.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.hoang8f.widget.FButton;

public class MainActivity extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnSignIn,btnSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        //btnSignIn.setButtonColor(R.color.colorPrimary);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_User = database.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(MainActivity.this,signUp.class);
                startActivity(signUp);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                table_User.addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (edtPhone.getText().toString().length() != 0) {
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                                if (user.getPassword().equals(edtPassword.getText().toString())) {
                                    Toast.makeText(MainActivity.this, "Sign in Successfully!", Toast.LENGTH_LONG).show();
                                    Common.currentUser = user;
                                    Intent intent = new Intent(MainActivity.this,main_category.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(MainActivity.this, "Sign in Failed!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "no User!", Toast.LENGTH_LONG).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
