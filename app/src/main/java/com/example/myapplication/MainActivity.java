package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView kunaImageView;
    Button button;
    View clickViewLeft;
    View clickViewRight;

    ImageView leftIce;
    ImageView rightIce;
    Guideline guideline;
    static DisplayMetrics displayMetrics;
    static int height;
    static int width;

    static float leftOrRight = 0;
    static boolean lost = false;

    static float change = 0;
    static long startClid;
    static float boxChange = 5f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        Log.d("123456", "onCreate: " + height);
        CollisionDetection cd = new CollisionDetection();

        kunaImageView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        clickViewLeft = findViewById(R.id.clickViewLeft);
        clickViewRight = findViewById(R.id.clickViewRight);

        guideline = findViewById(R.id.guideline);
        leftIce = findViewById(R.id.left_ice_view);
        rightIce = findViewById(R.id.right_ice_view);


//        kunaImageView.setOnClickListener(v -> {
//            startClid = System.currentTimeMillis();
//            change = -17f;
//        });
//
        clickViewLeft.setOnClickListener(v -> {
            startClid = System.currentTimeMillis();
            change = -20f;
            leftOrRight = 0;
            leftOrRight += -6f;
        });

        clickViewRight.setOnClickListener(v -> {
            if (lost) {
                lost = false;
                leftIce.setY(-200f);
                rightIce.setY(-200f);
                kunaImageView.setX(width / 2);
                kunaImageView.setY(height / 2);
            }
            startClid = System.currentTimeMillis();
            change = -20f;
            leftOrRight = 0;
            leftOrRight += 6f;
        });


        leftIce.setX(0);
        rightIce.setX(0);


        button.setOnClickListener(v -> {
            Timer timer = new Timer();
            Log.d("123456", "onCreate: " + kunaImageView.getY());
            Log.d("123456", "onCreate: " + kunaImageView.getHeight());
            Log.d("123456", "onCreate: " + (kunaImageView.getY() + kunaImageView.getHeight()));

            kunaImageView.setY(height / 3);
            int delay = 16; // delay for 0.17 sec.
            int period = 16; // repeat every sec.

            change = 1f;

            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    long end = System.currentTimeMillis();
                    if (end - startClid < 400) {
//                        change-=1f;
                    }

                    if (leftIce.getY() >= height) {
                        Random r = new Random();
                        int i1 = r.nextInt(95) + 5;
                        runOnUiThread(() -> guideline.setGuidelinePercent((i1 / 100f)));
                        leftIce.setY(-200);
                        rightIce.setY(-200);
                    }


                    if (kunaImageView.getY() + kunaImageView.getHeight() >= height || kunaImageView.getY() < 0) {
                        timer.cancel();
                        timer.purge();
                        change = 0;
//                        kunaImageView.setY(height / 3);
                        Log.d("123456", "onCreateca: " + kunaImageView.getY());

                    } else {
                        if (cd.collision(kunaImageView, leftIce) || cd.collision(kunaImageView, rightIce)) {
                            timer.cancel();
                            timer.purge();
                            change = 0;
                            lost = true;
//                            kunaImageView.setY(height / 3);
                        } else {
                            change += 1;
                            kunaImageView.setY(kunaImageView.getY() + change);
                            kunaImageView.setX(kunaImageView.getX() + leftOrRight);
                            leftIce.setY(leftIce.getY() + boxChange);
                            rightIce.setY(leftIce.getY() + boxChange);
                        }
                    }
                }

            }, delay, period);


        });


    }


}
