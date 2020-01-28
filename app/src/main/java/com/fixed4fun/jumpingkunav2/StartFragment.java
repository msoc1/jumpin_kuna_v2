package com.fixed4fun.jumpingkunav2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fixed4fun.jumpingkunav2.GameFragments.GameFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {


    public StartFragment() {
        // Required empty public constructor
    }

    Button startGame;
    Button logInRegister;
    ImageView kuna;
    static float change = 0f;
    static float boxChange = 0f;
    ImageView left;
    ImageView right;
    private FirebaseAuth firebaseAuth;
    private String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);


        change = 1f;
        boxChange = 20f;
        int delay = 16; // delay for 0.17 sec.
        int period = 16; // repeat every sec.
        startGame = view.findViewById(R.id.start_game);
        kuna = view.findViewById(R.id.kuna);
        left = view.findViewById(R.id.main_leftice);
        right = view.findViewById(R.id.main_rightice);
        logInRegister = view.findViewById(R.id.login_register);
        GameFragment gameFragment = new GameFragment();
        RegistrationFragment registrationFragment = new RegistrationFragment();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            String fullName = firebaseAuth.getCurrentUser().getEmail();
            username = fullName.split("@")[0];
        } else {
            username = "";
        }

        if (firebaseAuth.getCurrentUser() == null) {
            logInRegister.setText("log in");
        } else {
            String fullName = firebaseAuth.getCurrentUser().getEmail();
            username = fullName.split("@")[0];
            logInRegister.setText("log out " + username);
        }

        Timer jumpInBackground = new Timer();
        startGame.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, gameFragment).commit();
            jumpInBackground.purge();
            jumpInBackground.cancel();
        });

        logInRegister.setOnClickListener(v -> {
            if (firebaseAuth.getCurrentUser() == null) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, registrationFragment).commit();
                jumpInBackground.purge();
                jumpInBackground.cancel();
            } else {
                String fullName = firebaseAuth.getCurrentUser().getEmail();
                username = fullName.split("@")[0];
                Toast.makeText(getContext(), "Signing out" + " " + username, Toast.LENGTH_SHORT).show();
                logInRegister.setText("SIGN IN");
                firebaseAuth.signOut();
            }
        });


        jumpInBackground.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                change += 1f;
                left.setY(left.getY() + boxChange);
                right.setY(left.getY() + boxChange);


                if (left.getY() > 2100) {
                    left.setY(-300);
                    right.setY(-300);
                }
                kuna.setY(kuna.getY() + change);

                if (kuna.getY() > 1300) {
                    change *= -1;
                    kuna.setY(kuna.getY() - 45f);
                }

            }
        }, delay, period);


        return view;

    }


    @Override
    public void onResume() {
        super.onResume();


    }

}
