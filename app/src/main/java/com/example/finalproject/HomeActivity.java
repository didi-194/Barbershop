package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment;
    OrderFragment orderFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String status;
        Intent intent = getIntent();
        status = intent.getStringExtra("status");

        bottomNavigationView = findViewById(R.id.bottom_nav);

        homeFragment = new HomeFragment();
        orderFragment = new OrderFragment();
        profileFragment = new ProfileFragment();

        if (status == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        }
        else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, orderFragment).commit();
//            bottomNavigationView.findViewById(R.id.profile).isSelected();
        }


        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                    return true;

                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment).commit();
                    return true;

                case R.id.history:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, orderFragment).commit();
                    return true;
            }


            return false;
        });

    }
}