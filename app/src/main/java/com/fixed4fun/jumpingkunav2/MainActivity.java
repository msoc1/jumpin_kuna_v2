package com.fixed4fun.jumpingkunav2;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fixed4fun.jumpingkunav2.GameFragments.GameFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
   Button startGame;
   ImageView kuna;
    static float change = 0f;
    static float boxChange = 0f;
    ImageView left;
    ImageView right;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        change =1f;
        boxChange = 20f;
        int delay = 16; // delay for 0.17 sec.
        int period = 16; // repeat every sec.
        startGame = findViewById(R.id.start_game);
        kuna = findViewById(R.id.kuna);
        left = findViewById(R.id.main_leftice);
        right = findViewById(R.id.main_rightice);
        GameFragment gameFragment = new GameFragment();

        Timer jumpInBackground = new Timer();
        startGame.setOnClickListener( v -> {
            getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
            jumpInBackground.purge();
            jumpInBackground.cancel();
        } );

        jumpInBackground.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                change+=1f;
                left.setY(left.getY()+boxChange);
                right.setY(left.getY()+boxChange);



                if(left.getY()>2100){
                    left.setY(-300);
                    right.setY(-300);
                }
                kuna.setY(kuna.getY()+change);

                if(kuna.getY() > 1300){
                    change*=-1;
                    kuna.setY(kuna.getY()-45f);
                }

            }
        }, delay, period);


    }

}

