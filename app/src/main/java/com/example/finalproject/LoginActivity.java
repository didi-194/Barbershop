package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText et_email, et_password;
    Button btn_login;
    TextView tv_register;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        tv_register = findViewById(R.id.tv_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        //If the user has signed in before
        if (mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }


        btn_login.setOnClickListener(v -> loginUser());
        tv_register.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, RegisterActivity.class));
        });

    }

    void loginUser(){
        String email, pass;

        email = et_email.getText().toString();
        pass = et_password.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(this, "User Logged in", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, HomeActivity.class));
            }else {
                Toast.makeText(this, "Failed Logging in", Toast.LENGTH_SHORT).show();
                et_email.setText("");
                et_password.setText("");
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }

}