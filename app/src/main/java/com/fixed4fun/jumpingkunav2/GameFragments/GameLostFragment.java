package com.fixed4fun.jumpingkunav2.GameFragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fixed4fun.jumpingkunav2.MainActivity;
import com.fixed4fun.jumpingkunav2.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameLostFragment extends Fragment {


    public GameLostFragment() {
        // Required empty public constructor
    }

    private Button newGame;
    private Button goToHome;
    private TextView yourScore;
    private TextView yourTime;
    private GameFragment gameFragment;

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
            String timeToSet = time/1000 + ":" +  time%1000 + "s";
            yourTime.setText(timeToSet);
        }


        gameFragment = new GameFragment();
        newGame = inflatedView.findViewById(R.id.new_game_from_lost);
        newGame.setOnClickListener(va -> {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
        });

        goToHome.setOnClickListener( va -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        });


        return inflatedView;
    }

}
