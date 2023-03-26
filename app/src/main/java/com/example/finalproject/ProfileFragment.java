package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    EditText et_user_email, et_user_name, et_old_password, et_new_password;
    Button btn_change_pass, btn_logout;

    FirebaseAuth mAuth;
    DatabaseReference db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        et_user_email = view.findViewById(R.id.et_user_email);
        et_user_name = view.findViewById(R.id.et_user_name);
        et_old_password = view.findViewById(R.id.et_old_password);
        et_new_password = view.findViewById(R.id.et_new_password);
        btn_change_pass = view.findViewById(R.id.btn_change_pass);
        btn_logout = view.findViewById(R.id.btn_logout);

        mAuth = FirebaseAuth.getInstance();

        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db = FirebaseDatabase
                .getInstance("https://finalproject-38e5c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User").child(id).child("name");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                et_user_name.setText(Objects.requireNonNull(snapshot.getValue()).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        et_user_email.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());

        btn_change_pass.setOnClickListener(view1 -> {
            String old_p, new_p;
            old_p = et_old_password.getText().toString();
            new_p = et_new_password.getText().toString();

            mAuth.signInWithEmailAndPassword(Objects.requireNonNull(mAuth.getCurrentUser().getEmail()), old_p).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    // pass correct
                    mAuth.getCurrentUser().updatePassword(new_p);
                    Toast.makeText(getActivity(), "Password Successfully Changed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btn_logout.setOnClickListener(view1 -> {
            mAuth.signOut();
            getActivity().finish();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

    }
}