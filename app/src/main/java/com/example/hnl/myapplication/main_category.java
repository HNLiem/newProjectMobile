package com.example.hnl.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hnl.myapplication.Common.Common;
import com.example.hnl.myapplication.fragment.CartFragment;
import com.example.hnl.myapplication.fragment.CategoryFragment;
import com.example.hnl.myapplication.fragment.UserFragment;
import com.example.hnl.myapplication.fragment.add_new_brand;
import com.example.hnl.myapplication.fragment.load_request;

public class main_category extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle adtl;
    private ImageView open_nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_category);
        View overlay = findViewById(R.id.myview);
        overlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        dl = (DrawerLayout) findViewById(R.id.dl);
        adtl = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        adtl.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(adtl);
        adtl.syncState();

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_main);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.myprofile)
                {
                    Fragment frag = new UserFragment();
                    addFragment(frag);
                }
                else if(id == R.id.brand_category)
                {
                    Fragment frag = new CategoryFragment();
                    addFragment(frag);
                }
                else if(id == R.id.cart)
                {
                    Fragment frag = new CartFragment();
                    addFragment(frag);
                }
                else if(id == R.id.add_new_brand)
                {
                    if(Common.currentUser.getType().equals("Customer"))
                    {
                        Toast.makeText(getBaseContext(),"Only Admin Account Can Use This Tool",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Fragment frag = new add_new_brand();
                        addFragment(frag);
                    }
                }
                else if(id == R.id.load_rq)
                {
                    if(Common.currentUser.getType().equals("Customer"))
                    {
                        Toast.makeText(getBaseContext(),"Only Admin Account Can Use This Tool",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Fragment frag = new load_request();
                        addFragment(frag);
                    }
                }
                else if(id == R.id.log_out)
                {
                    finish();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertdialog=new AlertDialog.Builder(this);
        alertdialog.setTitle("Log Out");
        LayoutInflater inflater=this.getLayoutInflater();
        View add_shoe_layout=inflater.inflate(R.layout.logout_yes_no,null);
        alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return adtl.onOptionsItemSelected(item)|| super.onOptionsItemSelected(item);
    }

    public void addFragment(Fragment chose)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer,chose);
        fragmentTransaction.commit();
    }

}
