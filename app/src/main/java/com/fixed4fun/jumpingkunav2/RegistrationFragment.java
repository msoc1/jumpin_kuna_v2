package com.fixed4fun.jumpingkunav2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {


    public RegistrationFragment() {
        // Required empty public constructor
    }

    EditText usernameEditText;
    EditText passwordEditText;
    Button register;
    Button login;
    Button home;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        container.removeAllViews();
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        usernameEditText = view.findViewById(R.id.username_edittext);
        passwordEditText = view.findViewById(R.id.password_edittext);
        register = view.findViewById(R.id.register_button);
        login = view.findViewById(R.id.login_button);
        home = view.findViewById(R.id.home_button);

        home.setOnClickListener(v -> {
            StartFragment startFragment = new StartFragment();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
        });


        return view;
    }

}
