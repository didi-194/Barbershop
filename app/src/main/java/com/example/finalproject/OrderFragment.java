package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class OrderFragment extends Fragment {

    FirebaseAuth mAuth;
    DatabaseReference db;

    ListView listView;
    ArrayList <Order> vOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        db = FirebaseDatabase
                .getInstance("https://finalproject-38e5c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Order").child(id);

        listView = view.findViewById(R.id.order_list);
        vOrder = new ArrayList<>();

    }

    @Override
    public void onStart() {
        super.onStart();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                vOrder.clear();

                for (DataSnapshot orderSnapshot : snapshot.getChildren()){

                    Order order = orderSnapshot.getValue(Order.class);
                    vOrder.add(order);
                }

                Collections.reverse(vOrder);

                OrderAdapter adapter = new OrderAdapter(getActivity(), vOrder);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}