package com.example.hnl.myapplication.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hnl.myapplication.MainActivity;
import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.item_class.Category;
import com.example.hnl.myapplication.main_category;
import com.example.hnl.myapplication.shoes_list;
import com.example.hnl.myapplication.viewHolder.MenuViewHolder;
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

import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class add_new_brand extends Fragment {

    float pt;
    ImageView img_new_brand;
    Button btnSelectImage,btnUpload,btfinish;
    EditText edtNewBrand;
    FirebaseDatabase database;
    DatabaseReference category;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    private final int PICK_IMAGE_REQUEST=71;
    Uri uri;
    FirebaseStorage storage;
    StorageReference storageReference;
    Category newCategory;
    String name;

    public add_new_brand() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot =  inflater.inflate(R.layout.fragment_add_new_brand, container, false);
        img_new_brand = (ImageView) viewRoot.findViewById(R.id.img_new_brand);
        btnSelectImage = (Button) viewRoot.findViewById(R.id.btselect);
        btnUpload = (Button) viewRoot.findViewById(R.id.btupload);
        edtNewBrand = (EditText) viewRoot.findViewById(R.id.txtnewname);
        database=FirebaseDatabase.getInstance();
        category=database.getReference("category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtNewBrand.getText().toString();
                uploadImage();
                if(pt==100)
                {
                    if(newCategory!=null)
                    {
                        category.push().setValue(newCategory);
                        Toast.makeText(getContext(),"Finish",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getContext(),"Full Fill Information",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });




        return viewRoot;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null && data.getData()!=null)
        {
            uri=data.getData();
            img_new_brand.setImageURI(uri);

            btnSelectImage.setText("Image Selected");
            btnSelectImage.setEnabled(false);
        }
    }

    public void chooseImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);
    }

    public void uploadImage()
    {
        if(uri!=null)
        {
            final ProgressDialog mDialog=new ProgressDialog(getActivity());
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(),"Uploaded",Toast.LENGTH_SHORT).show();

                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory=new Category(name,uri.toString());
                        }
                    });
                    btnUpload.setText("Finish");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progess=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Upload "+(int)progess+"%");
                    pt=(float) progess;
                }
            });

        }
    }
}
