package com.fixed4fun.jumpingkunav2.GameFragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fixed4fun.jumpingkunav2.MainActivity;
import com.fixed4fun.jumpingkunav2.R;
import com.fixed4fun.jumpingkunav2.Score;
import com.fixed4fun.jumpingkunav2.StartFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameLostFragment extends Fragment {


    public GameLostFragment() {
        // Required empty public constructor
    }

    private Button newGame;
    private Button goToHome;
    Button share;
    private TextView yourScore;
    private TextView yourTime;
    private GameFragment gameFragment;
    private StartFragment startFragment;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mFirebaseDatabaseInstance;
    private DatabaseReference mFirebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_game_lost, container, false);
        container.removeAllViews();

        Bundle bundle = this.getArguments();

        yourScore = inflatedView.findViewById(R.id.your_score_textview);
        yourTime = inflatedView.findViewById(R.id.time_textview);
        goToHome = inflatedView.findViewById(R.id.goto_home);
        if (bundle != null) {
            yourScore.setText(String.valueOf(bundle.getInt("SCORE")));
            int time = bundle.getInt("TIME");
            String timeToSet = time / 1000 + ":" + time % 1000 + "s";
            yourTime.setText(timeToSet);
        }

        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseInstance = FirebaseDatabase.getInstance();

        if (bundle.getInt("SCORE") >= 0) {
            String username;
            if (firebaseAuth.getCurrentUser() == null) {
                username = "anonymous";
            } else {
                String fullName = firebaseAuth.getCurrentUser().getEmail();
                username = fullName.split("@")[0];
            }

            Score newRecord = new Score(bundle.getInt("TIME"), bundle.getInt("SCORE"));
            mFirebaseDatabase = mFirebaseDatabaseInstance.getReference("usrscr/".concat(username));
            mFirebaseDatabase.push().setValue(newRecord);
        }

        gameFragment = new GameFragment();
        startFragment = new StartFragment();
        newGame = inflatedView.findViewById(R.id.new_game_from_lost);
        newGame.setOnClickListener(va -> {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
        });

        goToHome.setOnClickListener(va -> {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
        });

        share = inflatedView.findViewById(R.id.share_score);
        share.setOnClickListener(v -> {
            int time = bundle.getInt("TIME");
            String timeToSet = time / 1000 + ":" + time % 1000 + "s";
            String message = "I scored " + bundle.getInt("SCORE") + " in " + timeToSet + " playing \n\tJumpin Kuna!" + "\n"
                    + " play.google.com/store/apps/details?id=com.fixed4fun.jumpinkuna";
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Share Jumpin Kuna!"));
        });


        return inflatedView;
    }

}
