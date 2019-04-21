package com.example.hnl.myapplication.fragment;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hnl.myapplication.Common.Common;
import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.interfaces.ItemClickListener;
import com.example.hnl.myapplication.item_class.Category;
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
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CategoryFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    Common common;
    EditText textname;
    Button upload;
    Button select;
    FloatingActionButton addNewCategory;
    FirebaseDatabase database;
    DatabaseReference category;
    RecyclerView recycler_menu;
    Category newCategory;
    Uri uri;
    float pt;
    private final int PICK_IMAGE_REQUEST=71;
    FirebaseStorage storage;
    StorageReference storageReference;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    public CategoryFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot =  inflater.inflate(R.layout.fragment_category, container, false);
        // Inflate the layout for this fragment


        database=FirebaseDatabase.getInstance();
        category=database.getReference("category");
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        recycler_menu=(RecyclerView)viewRoot.findViewById(R.id.recyclerview_menu);
        recycler_menu.setHasFixedSize(true);
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recycler_menu.setLayoutManager(gridLayoutManager);
        loadMenu();
        /*addNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogadd();
            }
        });*/
        return viewRoot;
    }
/*private void showdialogadd()
{

    AlertDialog.Builder alertdialog=new AlertDialog.Builder(getActivity());
    alertdialog.setTitle("ADD NEW SHOES");
    alertdialog.setMessage("Please fill full information");
    LayoutInflater inflater=this.getLayoutInflater();
    View add_menu_layout=inflater.inflate(R.layout.add_menu_category,null);

    textname=add_menu_layout.findViewById(R.id.txtnewname);
    select=add_menu_layout.findViewById(R.id.btselect);
    upload=add_menu_layout.findViewById(R.id.btupload);
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
    alertdialog.setView(add_menu_layout);
    alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if(newCategory!=null)
            {
                category.push().setValue(newCategory);
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



}*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST && resultCode ==RESULT_OK && data !=null && data.getData()!=null)
        {
            uri=data.getData();
            select.setText("Image Selected");
        }
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
            final ProgressDialog mDialog=new ProgressDialog(getActivity());
            mDialog.setMessage("Uploading...");
            mDialog.show();
            String imageName=UUID.randomUUID().toString();
            final StorageReference imageFolder=storageReference.child("images/"+imageName);
            imageFolder.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mDialog.dismiss();
                    Toast.makeText(getActivity(),"Upload!!!",Toast.LENGTH_SHORT).show();
                    imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory=new Category(textname.getText().toString(),uri.toString());
                        }
                    });
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
                    mDialog.setMessage("Upload"+progess+"%");
                    pt=(float) progess;
                }
            });

        }
    }
    private void loadMenu() {
        Query query = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("category");

        FirebaseRecyclerOptions<Category> options =
                new FirebaseRecyclerOptions.Builder<Category>()
                        .setQuery(query, Category.class)
                        .build();
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.shoes_brand_category_item, parent, false);

                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
                holder.txtMenuName.setText(model.getName());
                Picasso.with( getActivity().getBaseContext()).load(model.getImage() )
                        .into(holder.imageView);
                final Category clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent newintent = new Intent(getActivity(),shoes_list.class);
                        newintent.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(newintent);


                    }
                });
            }

        };
        adapter.getItemCount();
        recycler_menu.setAdapter(adapter);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(Common.currentUser.getType().equals("Customer"))
        {
            Toast.makeText(getActivity(),"Only Admin Account Can Use This Tool",Toast.LENGTH_LONG).show();
        }
        else
        {
            if (item.getTitle().equals(common.UPDATE)) {
                showUpdateDialog(adapter.getRef(item.getOrder()).getKey(), adapter.getItem(item.getOrder()));
                Toast.makeText(getActivity(), "Update Complete", Toast.LENGTH_LONG).show();

            } else if (item.getTitle().equals(common.DELETE)) {
                deleteCategory(adapter.getRef(item.getOrder()).getKey());
                Toast.makeText(getActivity(), "Delete Complete", Toast.LENGTH_LONG).show();
            }
        }
        return super.onContextItemSelected(item);
    }

    private void deleteCategory(String key) {
        category.child(key).removeValue();
    }

    private void showUpdateDialog(final String key, final Category item) {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(getActivity());
        alertdialog.setTitle("Update Brand");
        alertdialog.setMessage("Please fill full information");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_menu_layout=inflater.inflate(R.layout.update_category,null);

        textname=add_menu_layout.findViewById(R.id.txtnewname);
        select=add_menu_layout.findViewById(R.id.btselect);
        upload=add_menu_layout.findViewById(R.id.btupload);
        textname.setText(item.getName());
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
        alertdialog.setView(add_menu_layout);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                item.setName(textname.getText().toString());
                category.child(key).setValue(item);

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

    private void changeImage(final Category item) {
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
                    Toast.makeText(getActivity(),"Upload!!!",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(),""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progess=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Upload"+(int)progess+"%");
                }
            });

        }
    }
}
