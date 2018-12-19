package com.example.hnl.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.hnl.myapplication.item_class.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signUp extends AppCompatActivity {

    EditText edtPhone,edtPassword,edtName;
    Button btnRegister,btnCancel;
    RadioGroup radioGroup;
    RadioButton rdbSeller,rdbCustomer;
    Byte type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtName = (EditText) findViewById(R.id.edtName);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        radioGroup = (RadioGroup) findViewById(R.id.rdbChoseGroup);
        rdbSeller = (RadioButton) findViewById(R.id.rdbSeller);
        rdbCustomer = (RadioButton) findViewById(R.id.rdbCustomer);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_User = database.getReference("User");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                table_User.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            Toast.makeText(signUp.this,"Số điện thoại đã tồn tại",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            if(edtPhone.getText().toString().length() < 10 || edtPhone.getText().toString().length() > 11)
                            {
                                Toast.makeText(signUp.this,"Số điện thoại không hợp lê",Toast.LENGTH_LONG).show();

                            }
                            if(edtName.getText().toString().length() < 4)
                            {
                                Toast.makeText(signUp.this,"Tên đăng nhập không hợp lệ",Toast.LENGTH_LONG).show();

                            }
                            else
                            {
                                if(edtPassword.getText().toString().length() <4)
                                {
                                    Toast.makeText(signUp.this,"Mật khẩu không hợp lệ",Toast.LENGTH_LONG).show();

                                }
                                else
                                {

                                    if(rdbCustomer.isChecked()==false && rdbSeller.isChecked()==false)
                                    {
                                        Toast.makeText(signUp.this,"Vui lòng nhập đầy đủ thông tin",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                switch (checkedId)
                                                {
                                                    case R.id.rdbCustomer:
                                                        type=1;
                                                        break;
                                                    case R.id.rdbSeller:
                                                        type=2;
                                                        break;
                                                }
                                            }
                                        });
                                        User user=new User(edtName.getText().toString(),edtPassword.getText().toString(),type);
                                        table_User.child(edtPhone.getText().toString()).setValue(user);
                                        Toast.makeText(signUp.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                                        finish();
                                    }



                                }
                            }

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
