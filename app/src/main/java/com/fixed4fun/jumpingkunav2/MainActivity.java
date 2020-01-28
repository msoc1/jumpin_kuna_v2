package com.fixed4fun.jumpingkunav2;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartFragment startFragment = new StartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();

    }

}

