package com.fixed4fun.jumpingkunav2.GameFragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fixed4fun.jumpingkunav2.CollisionDetection;
import com.fixed4fun.jumpingkunav2.R;

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
    Button startGameButton;
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
    ImageView bonusBubble;
    TextView counter;


    static float change = 0;
    static long startClid;
    //TODO change obstacle speed and distance based on user score
    static float boxChange = 10f;
    int discanceBetweenIces = 800;
    static ViewGroup.MarginLayoutParams mlp;
    int activeObstacle = 0;
    int score;
    static int currentScore = 0;
    long runStartTime;
    long runEndTime;
    static boolean gotImmortalityBonus = false;
    Timer collisionDetectionThread = new Timer();
    Timer gameThread = new Timer();


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
            change = -20f;
            leftOrRight = 0;
            leftOrRight += -6f;
        });

        clickViewRight.setOnClickListener(v -> {
            change = -20f;
            leftOrRight = 0;
            leftOrRight += 6f;
        });


        startGameButton.setOnClickListener(v -> {
            startGameButton.setVisibility(View.INVISIBLE);
            collisionDetectionThread = new Timer();
            gameThread = new Timer();
            kunaImageView.setY(height / 3);
            int delay = 16; // delay for 0.17 sec.
            int period = 16; // repeat every sec.
            change = 1f;
            setUpBeggining(randomForGuidelines);
            runStartTime = System.currentTimeMillis();


            collisionDetectionThread.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    //commented for testing purposes
//                    if (!immortalityBool && wasCollision(cd)) {
//                        gameThread.cancel();
//                        gameThread.purge();
//                        collisionDetectionThread.cancel();
//                        collisionDetectionThread.purge();
//                        gameLost();
                    if (!gotImmortalityBonus && cd.collision(kunaImageView, bonusBubble)) {
                        getActivity().runOnUiThread(() -> counter.setVisibility(View.VISIBLE));
                        currentScore = score;
                        startClid = System.currentTimeMillis();
                        gotImmortalityBonus = true;
                        if (bonusBubble.getId() == 1111) {
                            immortalityBool = true;
                            kunaImageView.setAlpha(0.5f);
                        } else if (bonusBubble.getId() == 2222) {
                            boxChange /= 2f;
                        }
//                        getActivity().runOnUiThread(() -> bonusBubble.setVisibility(View.INVISIBLE));
                    }
                }
            }, delay, period);


            gameThread.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    addPoints();
                    long end = System.currentTimeMillis();
                    if (end - startClid >= 0) {
                        getActivity().runOnUiThread(() -> counter.setText(String.valueOf(10 - (end - startClid) / 1000)));
                    }

                    if (gotImmortalityBonus) {
                        if (end - startClid >= 10000) {
                            immortalityBool = false;
                            gotImmortalityBonus = false;
                            kunaImageView.setAlpha(1f);
                            getActivity().runOnUiThread(() -> counter.setVisibility(View.INVISIBLE));
                            getActivity().runOnUiThread(() -> bonusBubble.setVisibility(View.VISIBLE));
                            boxChange = 10f;
                        }
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
                    //TODO adjust height * 2 to 5-6, its 2 for testing
                    if (bonusBubble.getY() >= height * 2) {
                        Random r = new Random();
                        int i1 = r.nextInt(90);
                        bonusBubble.setX((i1 / 100f) * width);
                        Log.d("123456", "run: " + i1);
                        bonusBubble.setY(leftIce.getY() - leftIce.getHeight() - discanceBetweenIces * 3);
                        if (i1 > 50) {
                            bonusBubble.setImageResource(R.drawable.hourglass);
                            bonusBubble.setId(2222);
                        } else {
                            bonusBubble.setImageResource(R.drawable.immortality_bubble);
                            bonusBubble.setId(1111);
                        }
                    }


                    if (kunaImageView.getX() + 5 < 0 || kunaImageView.getX() + kunaImageView.getWidth() - 5 > width) {
                        leftOrRight *= -1;
                    }

                    if (kunaImageView.getY() < 0) {
                        change = 1;
                    }

                    //stop if player hits bottom
                    if (kunaImageView.getY() + kunaImageView.getHeight() >= height) {
                        gameLost();
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
                            bonusBubble.setY(bonusBubble.getY() + boxChange);
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

    public void gameLost() {
        runEndTime = System.currentTimeMillis();
        int runTime = (int) (runEndTime - runStartTime);
        gameThread.cancel();
        gameThread.purge();
        collisionDetectionThread.cancel();
        collisionDetectionThread.purge();
        change = 0;
        boxChange = 10f;
        gotImmortalityBonus = false;
        Bundle bundle = new Bundle();
        bundle.putInt("SCORE", score);
        bundle.putInt("TIME", runTime);
        GameLostFragment gameLostFragment = new GameLostFragment();
        gameLostFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.constraint_main, gameLostFragment).commit();
    }

    public void setUpBeggining(Random r) {
        leftIce.setVisibility(View.VISIBLE);
        rightIce.setVisibility(View.VISIBLE);
        bonusBubble.setVisibility(View.VISIBLE);
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
        bonusBubble.setY(-570);
        i1 = r.nextInt(90);
        bonusBubble.setX((i1 / 100f) * width);
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
        startGameButton = view.findViewById(R.id.start_game_button);
        clickViewLeft = view.findViewById(R.id.clickViewLeft);
        clickViewRight = view.findViewById(R.id.clickViewRight);
        firstGuideline = view.findViewById(R.id.guideline);
        secondGuideline = view.findViewById(R.id.second_guide_line);
        thirdGuideLine = view.findViewById(R.id.third_guide_line);
        leftIce = view.findViewById(R.id.left_ice_view);
        rightIce = view.findViewById(R.id.right_ice_view);
        counter = view.findViewById(R.id.counter_text_view);
        counter.setVisibility(View.INVISIBLE);
        leftIce.setVisibility(View.INVISIBLE);
        rightIce.setVisibility(View.INVISIBLE);
        constraintLayout = view.findViewById(R.id.constraint);
        scoreTextView = view.findViewById(R.id.score_text);

        secondLeftIce = new ImageView(getContext());
        thirdLeftIce = new ImageView(getContext());
        secondRightIce = new ImageView(getContext());
        thirdRightIce = new ImageView(getContext());
        bonusBubble = new ImageView(getContext());
        secondLeftIce.setImageResource(R.drawable.left_ice);
        secondRightIce.setImageResource(R.drawable.right_ice);
        thirdLeftIce.setImageResource(R.drawable.left_ice);
        thirdRightIce.setImageResource(R.drawable.right_ice);
        bonusBubble.setImageResource(R.drawable.immortality_bubble);
        secondLeftIce.setY(leftIce.getY() - discanceBetweenIces - secondLeftIce.getHeight());
        secondRightIce.setY(secondLeftIce.getY());
        thirdLeftIce.setY(secondLeftIce.getY() - discanceBetweenIces - secondLeftIce.getHeight());
        thirdRightIce.setY(thirdLeftIce.getY());
        constraintLayout.addView(secondLeftIce);
        constraintLayout.addView(thirdLeftIce);
        constraintLayout.addView(secondRightIce);
        constraintLayout.addView(thirdRightIce);
        constraintLayout.addView(bonusBubble);
        bonusBubble.setVisibility(View.INVISIBLE);
        secondLeftIce.setId(328);
        secondRightIce.setId(528);
        thirdLeftIce.setId(428);
        thirdRightIce.setId(628);
        bonusBubble.setId(1111);
    }

    private void settingUpObstacleDimens() {
        constraintLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            secondLeftIce.getLayoutParams().width = leftIce.getWidth();
            secondLeftIce.getLayoutParams().height = leftIce.getHeight();
            secondLeftIce.requestLayout();
            secondRightIce.getLayoutParams().width = leftIce.getWidth();
            secondRightIce.getLayoutParams().height = leftIce.getHeight();
            secondRightIce.requestLayout();
            thirdLeftIce.getLayoutParams().width = leftIce.getWidth();
            thirdLeftIce.getLayoutParams().height = leftIce.getHeight();
            thirdLeftIce.requestLayout();
            thirdRightIce.getLayoutParams().width = leftIce.getWidth();
            thirdRightIce.getLayoutParams().height = leftIce.getHeight();
            thirdRightIce.requestLayout();
            bonusBubble.getLayoutParams().width = leftIce.getHeight() * 3 / 4;
            bonusBubble.getLayoutParams().height = leftIce.getHeight() * 3 / 4;
            bonusBubble.requestLayout();

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
            }
        } else if (activeObstacle == 1) {
            if (kunaImageView.getY() <= secondLeftIce.getY()) {
                activeObstacle = 2;
                score++;
            }
        } else {
            if (kunaImageView.getY() <= thirdLeftIce.getY()) {
                activeObstacle = 0;
                score++;
            }
        }
        getActivity().runOnUiThread(() -> scoreTextView.setText(String.valueOf(score)));
    }

    public boolean wasCollision(CollisionDetection cd) {
        return cd.collision(kunaImageView, leftIce) || cd.collision(kunaImageView, rightIce) ||
                cd.collision(kunaImageView, secondLeftIce) || cd.collision(kunaImageView, secondRightIce) ||
                cd.collision(kunaImageView, thirdLeftIce) || cd.collision(kunaImageView, thirdRightIce);
    }


}
