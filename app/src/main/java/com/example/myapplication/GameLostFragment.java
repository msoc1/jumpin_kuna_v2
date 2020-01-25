package com.example.myapplication;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameLostFragment extends Fragment {


    public GameLostFragment() {
        // Required empty public constructor
    }

    private Button newGame;
    private TextView yourScore;
    private GameFragment gameFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_game_lost, container, false);
        container.removeAllViews();

        Bundle bundle = this.getArguments();

        yourScore = inflatedView.findViewById(R.id.your_score_textview);
        if (bundle != null) {
            yourScore.setText(String.valueOf(bundle.getInt("SCORE")));
        }


        gameFragment = new GameFragment();
        newGame = inflatedView.findViewById(R.id.new_game_from_lost);
        newGame.setOnClickListener(va -> {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
        });


        return inflatedView;
    }

}
