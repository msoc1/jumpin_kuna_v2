package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
   Button startGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO add loop of Kuna jumping while on main screen
        //TODO inflate StartFragment instead of activity

        startGame = findViewById(R.id.start_game);
        GameFragment gameFragment = new GameFragment();

        startGame.setOnClickListener( v -> {
            getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
        } );

    }

}

