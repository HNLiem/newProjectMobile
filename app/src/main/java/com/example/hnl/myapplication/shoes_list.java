package com.example.hnl.myapplication;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hnl.myapplication.Common.Common;
import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.item_class.Category;
import com.example.hnl.myapplication.item_class.Shoe;
import com.example.hnl.myapplication.viewHolder.ShoeViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class shoes_list extends AppCompatActivity {

    Common common;
    FloatingActionButton addnewshoe;
    Button upload,select;
    EditText name,price,discount,description;
    Uri uri;
    private final int PICK_IMAGE_REQUEST=71;
    Shoe newshoe;
    FirebaseStorage storage;
    StorageReference storageReference;
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
        addnewshoe=(FloatingActionButton)findViewById(R.id.addnewshoes);

        //firebase
        database=FirebaseDatabase.getInstance();
        shoelist=database.getReference("Shoe");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
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
        addnewshoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.currentUser.getType().equals("Customer"))
                {
                    Toast.makeText(getBaseContext(),"Only Admin Account Can Use This Tool",Toast.LENGTH_LONG).show();
                }
                else
                {
                    showAddShoeDialog();
                }
            }
        });

    }

    private void showAddShoeDialog() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("Add New Shoes");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_shoe_layout=inflater.inflate(R.layout.add_new_shoe,null);

        name=add_shoe_layout.findViewById(R.id.txtnewname);
        price=add_shoe_layout.findViewById(R.id.txtnewprice);
        discount=add_shoe_layout.findViewById(R.id.txtnewdiscount);
        description=add_shoe_layout.findViewById(R.id.txtnewdescription);

        select=add_shoe_layout.findViewById(R.id.btselect);
        upload=add_shoe_layout.findViewById(R.id.btupload);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        alertdialog.setView(add_shoe_layout);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(newshoe!=null)
                {
                    shoelist.push().setValue(newshoe);
                }
            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

    private void chooseImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }
    private void uploadImage()
    {
        if(uri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(shoes_list.this,"Upload!!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newshoe=new Shoe();
                            newshoe.setDescription(description.getText().toString());
                            newshoe.setDiscount(discount.getText().toString());
                            newshoe.setImage(uri.toString());
                            newshoe.setMenuId(categoryId);
                            newshoe.setName(name.getText().toString());
                            newshoe.setPrice(price.getText().toString());

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(shoes_list.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progess=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Upload "+(int)progess+"%");
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null && data.getData()!=null)
        {
            uri=data.getData();
            select.setText("Image Selected");
        }
    }

    private void loadListShoe(String categoryId) {
        Query query = FirebaseDatabase .getInstance() .getReference("Shoe") .orderByChild("menuId").equalTo(categoryId);
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
                        Intent shoeDetail=new Intent(shoes_list.this,Shoe_Detail.class);
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(Common.currentUser.getType().equals("Customer"))
        {
            Toast.makeText(getBaseContext(),"Only Admin Account Can Use This Tool",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(item.getTitle().equals(common.UPDATE))
            {
                showUpdateShoeDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
            }
            else if(item.getTitle().equals(common.DELETE))
            {
                deleteShoe(adapter.getRef(item.getOrder()).getKey());
            }
        }

        return super.onContextItemSelected(item);
    }

    private void deleteShoe(String key) {
        shoelist.child(key).removeValue();
    }

    private void showUpdateShoeDialog(final String key, final Shoe item)
    {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("Update Shoes");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_shoe_layout=inflater.inflate(R.layout.add_new_shoe,null);

        name=add_shoe_layout.findViewById(R.id.txtnewname);
        price=add_shoe_layout.findViewById(R.id.txtnewprice);
        discount=add_shoe_layout.findViewById(R.id.txtnewdiscount);
        description=add_shoe_layout.findViewById(R.id.txtnewdescription);

        name.setText(item.getName());
        price.setText(item.getPrice());
        discount.setText(item.getDiscount());
        description.setText(item.getDescription());

        select=add_shoe_layout.findViewById(R.id.btselect);
        upload=add_shoe_layout.findViewById(R.id.btupload);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });
        alertdialog.setView(add_shoe_layout);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                item.setName(name.getText().toString());
                item.setDescription(description.getText().toString());
                item.setDiscount(discount.getText().toString());
                item.setPrice(price.getText().toString());
                shoelist.child(key).setValue(item);

            }
        });
        alertdialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

    private void changeImage(final Shoe item)
    {
        if(uri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(shoes_list.this,"Upload!!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(shoes_list.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progess=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Upload "+(int)progess+"%");
                }
            });

        }
    }
}
