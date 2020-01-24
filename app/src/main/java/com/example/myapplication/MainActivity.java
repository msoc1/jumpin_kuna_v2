package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
   Button startGame;
    static ConstraintLayout constraintLayoutMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startGame = findViewById(R.id.start_game);
        constraintLayoutMain = findViewById(R.id.constraint_main);
        GameFragment gameFragment = new GameFragment();

        startGame.setOnClickListener( v -> {
            getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
        } );



    }



}

