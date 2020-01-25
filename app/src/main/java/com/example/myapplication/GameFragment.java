package com.example.myapplication;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {


    public GameFragment() {
        // Required empty public constructor
    }

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

    //TODO change those to an ImageView[] for simplification
    ImageView secondLeftIce;
    ImageView secondRightIce;
    ImageView thirdLeftIce;
    ImageView thirdRightIce;

    TextView scoreTextView;
    ConstraintLayout constraintLayout;

    static float leftOrRight = 0;
    static boolean lost = false;


    static boolean immortalityBool = false;
    ImageView immortalityBubble;
    ImageView immortalityImageView;

    ImageView teleportImageView;

    static float change = 0;
    static long startClid;
    //TODO change obstacle speed and distance based on user score
    static float boxChange = 10f;
    int discanceBetweenIces = 800;
    static ViewGroup.MarginLayoutParams mlp;
    int activeObstacle = 0;
    int score;
    static int currentScore = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        container.removeAllViews();

        displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;


        CollisionDetection cd = new CollisionDetection();
        ConstraintSet constraintSet = new ConstraintSet();
        settingUpViews(view);
        constraintSet.clone(constraintLayout);
        mlp = (ViewGroup.MarginLayoutParams) leftIce.getLayoutParams();
        constraintSet.applyTo(constraintLayout);
//        getActivity().setContentView(constraintLayout);
        settingUpObstacleDimens();
        Random randomForGuidelines = new Random();


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
        });

        immortalityImageView.setOnClickListener(v -> {
            immortalityImageView.setClickable(false);
            immortalityBubble.setVisibility(View.VISIBLE);
            immortalityBool = true;
            currentScore = score;
            Handler h = new Handler();
            h.postDelayed(() -> {
                immortalityBool = false;
                immortalityImageView.setBackgroundColor(Color.parseColor("#e4cd05"));
                immortalityBubble.setVisibility(View.INVISIBLE);
            }, 2000);
        });

        teleportImageView.setOnClickListener(v -> kunaImageView.setY(kunaImageView.getY() + 500));

        button.setOnClickListener(v -> {
            Timer gameThread = new Timer();
            kunaImageView.setY(height / 3);
            int delay = 16; // delay for 0.17 sec.
            int period = 16; // repeat every sec.
            change = 1f;
            setUpBeggining(randomForGuidelines);

            //commented for testing purposes
//            Timer collisionDetectionThread = new Timer();
//            collisionDetectionThread.scheduleAtFixedRate(new TimerTask() {
//                @Override
//                public void run() {
//                    if (!immortalityBool && wasCollision(cd)) {
//                        Thread.currentThread().toString();
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


                    if (currentScore + 5 <= score) {
                        getActivity().runOnUiThread(() -> {
                            immortalityImageView.setClickable(true);
                            immortalityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                        });
                    }


                    //move obstacles back to the top
                    if (leftIce.getY() >= height) {
                        int i1 = randomForGuidelines.nextInt(80) + 10;
                        firstGuideline.setX((i1 / 100f) * width);
                        rightIce.setY(thirdRightIce.getY() - discanceBetweenIces - leftIce.getHeight());
                        leftIce.setY(rightIce.getY());
                        leftIce.setX(firstGuideline.getX() - mlp.rightMargin - leftIce.getWidth());
                        rightIce.setX(firstGuideline.getX() + mlp.rightMargin);
                    }
                    if (secondLeftIce.getY() >= height) {
                        int i1 = randomForGuidelines.nextInt(80) + 10;
                        secondGuideline.setX((i1 / 100f) * width);
                        secondRightIce.setY(rightIce.getY() - discanceBetweenIces - rightIce.getHeight());
                        secondLeftIce.setY(leftIce.getY() - discanceBetweenIces - leftIce.getHeight());
                        secondLeftIce.setX(secondGuideline.getX() - mlp.rightMargin - leftIce.getWidth());
                        secondRightIce.setX(secondGuideline.getX() + mlp.rightMargin);
                    }
                    if (thirdLeftIce.getY() >= height) {
                        moveObstaclesToTop(randomForGuidelines, thirdGuideLine, thirdLeftIce, thirdRightIce);
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
                        Bundle bundle = new Bundle();
                        bundle.putInt("SCORE", score);
                        GameLostFragment gameLostFragment = new GameLostFragment();
                        gameLostFragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.constraint_main, gameLostFragment).commit();
                    } else {
                        //game
                        change += 1.5;
                        getActivity().runOnUiThread(() -> {
                            kunaImageView.setY(kunaImageView.getY() + change);
                            kunaImageView.setX(kunaImageView.getX() + leftOrRight);
                            leftIce.setY(leftIce.getY() + boxChange);
                            rightIce.setY(leftIce.getY() + boxChange);
                            secondLeftIce.setY(secondLeftIce.getY() + boxChange);
                            secondRightIce.setY(secondLeftIce.getY() + boxChange);
                            thirdLeftIce.setY(thirdLeftIce.getY() + boxChange);
                            thirdRightIce.setY(thirdLeftIce.getY() + boxChange);
                            immortalityBubble.setX(kunaImageView.getX() - immortalityBubble.getWidth() / 2f + kunaImageView.getWidth() / 2f);
                            immortalityBubble.setY(kunaImageView.getY() - kunaImageView.getWidth() / 2f);
                        });
                    }
                }
            }, delay, period);
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        getActivity().runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
    }

    public void settingUpViews(View view) {
        kunaImageView = view.findViewById(R.id.kuna_image_view);
        button = view.findViewById(R.id.button);
        clickViewLeft = view.findViewById(R.id.clickViewLeft);
        clickViewRight = view.findViewById(R.id.clickViewRight);
        firstGuideline = view.findViewById(R.id.guideline);
        secondGuideline = view.findViewById(R.id.second_guide_line);
        thirdGuideLine = view.findViewById(R.id.third_guide_line);
        leftIce = view.findViewById(R.id.left_ice_view);
        rightIce = view.findViewById(R.id.right_ice_view);
        leftIce.setVisibility(View.INVISIBLE);
        rightIce.setVisibility(View.INVISIBLE);
        immortalityBubble = view.findViewById(R.id.immortality_circle);
        constraintLayout = view.findViewById(R.id.constraint);
        scoreTextView = view.findViewById(R.id.score_text);
        immortalityImageView = view.findViewById(R.id.immortality_image_view);
        immortalityBubble.setVisibility(View.INVISIBLE);
        teleportImageView = view.findViewById(R.id.teleport_image_view);
        secondLeftIce = new ImageView(getContext());
        thirdLeftIce = new ImageView(getContext());
        secondRightIce = new ImageView(getContext());
        thirdRightIce = new ImageView(getContext());
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
        secondLeftIce.setId(328);
        secondRightIce.setId(528);
        thirdLeftIce.setId(428);
        thirdRightIce.setId(628);
    }

    private void settingUpObstacleDimens() {
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
    }

    public void moveObstaclesToTop(Random r, View guideline, View leftIce, View rightIce) {
        //TODO simplify this method to work for N number of obstacles
        int i1 = r.nextInt(80) + 10;
        guideline.setX((i1 / 100f) * width);
        leftIce.setY(secondLeftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        rightIce.setY(secondLeftIce.getY() - discanceBetweenIces - leftIce.getHeight());
        leftIce.setX(guideline.getX() - mlp.rightMargin - leftIce.getWidth());
        rightIce.setX(guideline.getX() + mlp.rightMargin);
    }

    public void addPoints() {
        if (activeObstacle == 0) {
            if (kunaImageView.getY() <= leftIce.getY()) {
                activeObstacle = 1;
                score++;
                getActivity().runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
            }
        } else if (activeObstacle == 1) {
            if (kunaImageView.getY() <= secondLeftIce.getY()) {
                activeObstacle = 2;
                score++;
                getActivity().runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
            }
        } else {
            if (kunaImageView.getY() <= thirdLeftIce.getY()) {
                activeObstacle = 0;
                score++;
                getActivity().runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
            }
        }
    }

    public boolean wasCollision(CollisionDetection cd) {
        return cd.collision(kunaImageView, leftIce) || cd.collision(kunaImageView, rightIce) ||
                cd.collision(kunaImageView, secondLeftIce) || cd.collision(kunaImageView, secondRightIce) ||
                cd.collision(kunaImageView, thirdLeftIce) || cd.collision(kunaImageView, thirdRightIce);
    }


}
