package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
    View firstGuideline;
    View secondGuideline;
    View thirdGuideLine;
    static DisplayMetrics displayMetrics;
    static int height;
    static int width;

    ImageView secondLeftIce;
    ImageView secondRightIce;
    ImageView thirdLeftIce;
    ImageView thirdRightIce;

    ImageView immortality;
    TextView scoreTextView;
    ConstraintLayout constraintLayout;

    static float leftOrRight = 0;
    static boolean lost = false;

    static float change = 0;
    static long startClid;
    static float boxChange = 10f;
    int discanceBetweenIces = 800;
    static ViewGroup.MarginLayoutParams mlp;
    int activeObstacle = 0;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        CollisionDetection cd = new CollisionDetection();

        kunaImageView = findViewById(R.id.kuna_image_view);
        button = findViewById(R.id.button);
        clickViewLeft = findViewById(R.id.clickViewLeft);
        clickViewRight = findViewById(R.id.clickViewRight);

        firstGuideline = findViewById(R.id.guideline);
        secondGuideline = findViewById(R.id.second_guide_line);
        thirdGuideLine = findViewById(R.id.third_guide_line);
        leftIce = findViewById(R.id.left_ice_view);
        rightIce = findViewById(R.id.right_ice_view);
        leftIce.setVisibility(View.INVISIBLE);
        rightIce.setVisibility(View.INVISIBLE);
        immortality = findViewById(R.id.immortality_circle);
        constraintLayout = findViewById(R.id.constraint);
        scoreTextView = findViewById(R.id.score_text);
        secondLeftIce = new ImageView(this);
        thirdLeftIce = new ImageView(this);
        secondRightIce = new ImageView(this);
        thirdRightIce = new ImageView(this);
        secondLeftIce.setImageResource(R.drawable.left_ice);
        secondRightIce.setImageResource(R.drawable.right_ice);
        thirdLeftIce.setImageResource(R.drawable.left_ice);
        thirdRightIce.setImageResource(R.drawable.right_ice);
        secondLeftIce.setY(leftIce.getY() - discanceBetweenIces - secondLeftIce.getHeight());
        secondRightIce.setY(secondLeftIce.getY());
        thirdLeftIce.setY(secondLeftIce.getY() - discanceBetweenIces - secondLeftIce.getHeight());
        thirdRightIce.setY(thirdLeftIce.getY());
        constraintLayout.addView(secondLeftIce);
        constraintLayout.addView(thirdLeftIce);
        constraintLayout.addView(secondRightIce);
        constraintLayout.addView(thirdRightIce);
        ConstraintSet constraintSet = new ConstraintSet();
        secondLeftIce.setId(328);
        secondRightIce.setId(528);
        thirdLeftIce.setId(428);
        thirdRightIce.setId(628);
        constraintSet.clone(constraintLayout);
        mlp = (ViewGroup.MarginLayoutParams) leftIce.getLayoutParams();
        constraintSet.applyTo(constraintLayout);
        setContentView(constraintLayout);


        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            secondLeftIce.getLayoutParams().width = leftIce.getWidth();
            secondLeftIce.getLayoutParams().height = leftIce.getHeight();
            secondLeftIce.setBackgroundColor(Color.parseColor("#e4cd05"));
            secondLeftIce.requestLayout();

            secondRightIce.getLayoutParams().width = leftIce.getWidth();
            secondRightIce.getLayoutParams().height = leftIce.getHeight();
            secondRightIce.setBackgroundColor(Color.parseColor("#e4cd05"));
            secondRightIce.requestLayout();

            thirdLeftIce.getLayoutParams().width = leftIce.getWidth();
            thirdLeftIce.getLayoutParams().height = leftIce.getHeight();
            thirdLeftIce.setBackgroundColor(Color.parseColor("#ffffff"));
            thirdLeftIce.requestLayout();
            thirdRightIce.getLayoutParams().width = leftIce.getWidth();
            thirdRightIce.getLayoutParams().height = leftIce.getHeight();
            thirdRightIce.setBackgroundColor(Color.parseColor("#ffffff"));
            thirdRightIce.requestLayout();
        });

        Random r = new Random();


        clickViewLeft.setOnClickListener(v -> {
            startClid = System.currentTimeMillis();
            change = -20f;
            leftOrRight = 0;
            leftOrRight += -6f;

        });

        clickViewRight.setOnClickListener(v -> {
            startClid = System.currentTimeMillis();
            change = -20f;
            leftOrRight = 0;
            leftOrRight += 6f;
            immortality.setVisibility(View.VISIBLE);

        });


        button.setOnClickListener(v -> {
            Timer gameThread = new Timer();
            kunaImageView.setY(height / 3);
            int delay = 16; // delay for 0.17 sec.
            int period = 17; // repeat every sec.

            change = 1f;
            setUpBeggining(r);

            //commented for testing purposes
//            Timer collisionDetectionThread = new Timer();
//            collisionDetectionThread.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//
//                    if (wasCollision(cd)) {
//                        Thread.currentThread().toString();
//                        Log.d("123456", "run: " + Thread.currentThread().toString());
//                        gameThread.cancel();
//                        gameThread.purge();
//                        collisionDetectionThread.cancel();
//                        collisionDetectionThread.purge();
//                    }
//                }
//            }, delay, period);


            gameThread.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    addPoints();
                    long end = System.currentTimeMillis();
                    if (end - startClid < 400) {
//                        change-=1f;
                    }
                    //move obstacles back to the top
                    if (leftIce.getY() >= height) {
                        int i1 = r.nextInt(80) + 10;
                        firstGuideline.setX((i1 / 100f) * width);
                        rightIce.setY(thirdRightIce.getY() - discanceBetweenIces - leftIce.getHeight());
                        leftIce.setY(rightIce.getY());
                        leftIce.setX(firstGuideline.getX() - mlp.rightMargin - leftIce.getWidth());
                        rightIce.setX(firstGuideline.getX() + mlp.rightMargin);
                    }
                    if (secondLeftIce.getY() >= height) {
                        int i1 = r.nextInt(80) + 10;
                        secondGuideline.setX((i1 / 100f) * width);
                        secondRightIce.setY(rightIce.getY() - discanceBetweenIces - rightIce.getHeight());
                        secondLeftIce.setY(leftIce.getY() - discanceBetweenIces - leftIce.getHeight());
                        secondLeftIce.setX(secondGuideline.getX() - mlp.rightMargin - leftIce.getWidth());
                        secondRightIce.setX(secondGuideline.getX() + mlp.rightMargin);
                    }
                    if (thirdLeftIce.getY() >= height) {
                        moveObstaclesToTop(r, thirdGuideLine, thirdLeftIce, thirdRightIce);
                    }


                    if (kunaImageView.getX() + 5 < 0 || kunaImageView.getX() + kunaImageView.getWidth() - 5 > width) {
                        leftOrRight *= -1;
                    }

                    if (kunaImageView.getY() < 0) {
                        change = 1;
                    }

                    //stop if player hits bottom
                    if (kunaImageView.getY() + kunaImageView.getHeight() >= height) {
                        gameThread.cancel();
                        gameThread.purge();
                        change = 0;
                    } else {
                        //game
                        change += 1.5;
                        runOnUiThread(() -> {
                            kunaImageView.setY(kunaImageView.getY() + change);
                            kunaImageView.setX(kunaImageView.getX() + leftOrRight);
                            leftIce.setY(leftIce.getY() + boxChange);
                            rightIce.setY(leftIce.getY() + boxChange);
                            secondLeftIce.setY(secondLeftIce.getY() + boxChange);
                            secondRightIce.setY(secondLeftIce.getY() + boxChange);
                            thirdLeftIce.setY(thirdLeftIce.getY() + boxChange);
                            thirdRightIce.setY(thirdLeftIce.getY() + boxChange);
                            immortality.setX(kunaImageView.getX() - immortality.getWidth() / 2f + kunaImageView.getWidth() / 2f);
                            immortality.setY(kunaImageView.getY() - kunaImageView.getWidth() / 2f);
                        });
                    }
                }
            }, delay, period);
        });


    }


    public void setUpBeggining(Random r) {
        leftIce.setVisibility(View.VISIBLE);
        rightIce.setVisibility(View.VISIBLE);
        leftIce.setY(-200f);
        rightIce.setY(-200f);
        secondLeftIce.setY(leftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        secondRightIce.setY(leftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        thirdLeftIce.setY(secondLeftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        thirdRightIce.setY(secondLeftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        kunaImageView.setX(width / 2);
        kunaImageView.setY(height * 0.45f);
        int i1 = r.nextInt(80) + 10;
        firstGuideline.setX((i1 / 100f) * width);
        i1 = r.nextInt(80) + 10;
        secondGuideline.setX((i1 / 100f) * width);
        i1 = r.nextInt(80) + 10;
        thirdGuideLine.setX((i1 / 100f) * width);
        score = 0;
        activeObstacle = 0;

        leftIce.setX(firstGuideline.getX() - mlp.rightMargin - leftIce.getWidth());
        rightIce.setX(firstGuideline.getX() + mlp.rightMargin);
        secondLeftIce.setX(secondGuideline.getX() - mlp.rightMargin - leftIce.getWidth());
        secondRightIce.setX(secondGuideline.getX() + mlp.rightMargin);
        thirdLeftIce.setX(thirdGuideLine.getX() - mlp.rightMargin - thirdLeftIce.getWidth());
        thirdRightIce.setX(thirdGuideLine.getX() + mlp.rightMargin);
    }

    public void addPoints() {
        if (activeObstacle == 0) {
            if (kunaImageView.getY() <= leftIce.getY()) {
                activeObstacle = 1;
                score++;
                runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
            }
        } else if (activeObstacle == 1) {
            if (kunaImageView.getY() <= secondLeftIce.getY()) {
                activeObstacle = 2;
                score++;
                runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
            }
        } else {
            if (kunaImageView.getY() <= thirdLeftIce.getY()) {
                activeObstacle = 0;
                score++;
                runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
            }
        }
    }

    public boolean wasCollision(CollisionDetection cd) {
        return cd.collision(kunaImageView, leftIce) || cd.collision(kunaImageView, rightIce) ||
                cd.collision(kunaImageView, secondLeftIce) || cd.collision(kunaImageView, secondRightIce) ||
                cd.collision(kunaImageView, thirdLeftIce) || cd.collision(kunaImageView, thirdRightIce);
    }


    public void moveObstaclesToTop(Random r, View guideline, View leftIce, View rightIce) {
        int i1 = r.nextInt(80) + 10;
        guideline.setX((i1 / 100f) * width);
        leftIce.setY(secondLeftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        rightIce.setY(secondLeftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        leftIce.setX(guideline.getX() - mlp.rightMargin - leftIce.getWidth());
        rightIce.setX(guideline.getX() + mlp.rightMargin);
    }


}

