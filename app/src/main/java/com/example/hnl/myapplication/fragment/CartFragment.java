package com.example.hnl.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hnl.myapplication.Common.Common;
import com.example.hnl.myapplication.Database.Database;
import com.example.hnl.myapplication.MainActivity;
import com.example.hnl.myapplication.R;
import com.example.hnl.myapplication.item_class.Order;
import com.example.hnl.myapplication.item_class.Request;
import com.example.hnl.myapplication.main_category;
import com.example.hnl.myapplication.viewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference request;

    TextView txtTotalPrice;
    Button btnPlaceOrder;

    List<Order> Cart = new ArrayList<>();

    CartAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot =  inflater.inflate(R.layout.fragment_cart, container, false);

        database=FirebaseDatabase.getInstance();
        request=database.getReference("Requests");

        txtTotalPrice = (TextView) viewRoot.findViewById(R.id.txtToTalMoney);
        btnPlaceOrder = (Button) viewRoot.findViewById(R.id.btnPlaceOrder);
        recyclerView = (RecyclerView) viewRoot.findViewById(R.id.ListCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameqq = String.valueOf(System.currentTimeMillis());
                Request requestCart = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        txtTotalPrice.getText().toString(),
                        Cart,
                        nameqq
                );

                request.child(nameqq).setValue(requestCart);

                new Database(getContext()).cleanCart();

                Toast.makeText(getContext(),"Thank you",Toast.LENGTH_LONG).show();

                loadListShoes();
                Intent intent = new Intent(getContext(),main_category.class);
                startActivity(intent);
            }
        });

        loadListShoes();


        return viewRoot;
    }



    private void loadListShoes() {
        Cart = new Database(getContext()).getCarts();
        adapter = new CartAdapter(Cart,getContext());
        recyclerView.setAdapter(adapter);

        int total = 0;
        for(Order order:Cart)
        {
            total+= Integer.parseInt(order.getPrice());
        }
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));
    }

}
