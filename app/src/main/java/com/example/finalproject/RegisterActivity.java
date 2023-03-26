package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText et_name, et_email, et_password;
    Button btn_register;
    TextView tv_login;

    ProgressDialog pd;

    //Firebase
    DatabaseReference db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_register = findViewById(R.id.btn_login);
        tv_login = findViewById(R.id.tv_register);

        pd = new ProgressDialog(this);

        //Firebase
        db = FirebaseDatabase
                .getInstance("https://finalproject-38e5c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("User");

        mAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(v -> registerUser());
        tv_login.setOnClickListener(v ->{
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    void registerUser(){
        String name, email, pass;

        name = et_name.getText().toString();
        email = et_email.getText().toString();
        pass = et_password.getText().toString();

        User user = new User(name, email);

//        db.child("SomeID").setValue(user);
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful()){
//                        Toast.makeText(this, "Data has been uploaded to Database", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(this, "Failed to upload to Database", Toast.LENGTH_SHORT).show();
//                    }
//        });

        if (!name.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
            pd.setMessage("Registering User...");
            pd.show();
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
                pd.dismiss();
                if (task.isSuccessful()){
                    Toast.makeText(this, "User has been registered successfully", Toast.LENGTH_SHORT).show();

                    //Inputting UID to Firebase Database
                    String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    db.child(id).setValue(user);

                    finish();
                    startActivity(new Intent(this, HomeActivity.class));
                }
                else {
                    Toast.makeText(this, "Failed to register User", Toast.LENGTH_SHORT).show();
                    et_name.setText("");
                    et_email.setText("");
                    et_password.setText("");
                }
            }).addOnFailureListener(e -> {
                //Showing Error
                Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        else {
            Toast.makeText(this, "name, email, password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        

    }

}