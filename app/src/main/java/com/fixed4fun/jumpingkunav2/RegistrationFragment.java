package com.fixed4fun.jumpingkunav2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


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
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String username;
    private String password;
    private FirebaseDatabase mFirebaseDatabaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private String userID;
    private String userEmail;


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
        firebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabaseInstance = FirebaseDatabase.getInstance();

        register.setOnClickListener(v -> {
            username = usernameEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(getContext(), "Enter username!", Toast.LENGTH_SHORT).show();
            } else if (username.length() > 14) {
                Toast.makeText(getContext(), "Username too long!", Toast.LENGTH_SHORT).show();
            } else if (password.isEmpty()) {
                Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            } else if (password.length() < 6) {
                Toast.makeText(getContext(), "Enter longer password!", Toast.LENGTH_SHORT).show();
            } else if (username.contains(" ")) {
                Toast.makeText(getContext(), "Spaces not allowed!", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.createUserWithEmailAndPassword(username + "@jumpinkuna.pl", password)
                        .addOnSuccessListener(task -> {
                            mFirebaseDatabase = mFirebaseDatabaseInstance.getReference("users");
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            userID = user.getUid();
                            userEmail = username + "@jumpinkuna.pl";
                            mFirebaseDatabase.child(userID).setValue(userEmail);

                            Toast.makeText(getContext(), "Registered", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(task -> {
                    Toast.makeText(getContext(), "Username taken", Toast.LENGTH_SHORT).show();

                });
            }
        });


        login.setOnClickListener(v -> {
            username = usernameEditText.getText().toString().trim() + "@jumpinkuna.pl";
            String user = usernameEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            if (user.length() == 0) {
                Toast.makeText(getContext(), "Enter username!", Toast.LENGTH_LONG).show();
            } else if (password.length() == 0) {
                Toast.makeText(getContext(), "Enter password!", Toast.LENGTH_LONG).show();
            } else {
                firebaseAuth.signInWithEmailAndPassword(username, passwordEditText.getText().toString().trim())
                        .addOnCompleteListener((task) -> {
                            if (!task.isSuccessful()) {
                                Toast.makeText(getContext(), "Cannot log in" + " " + usernameEditText.getText().toString(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), "Logged in!", Toast.LENGTH_LONG).show();
                                StartFragment startFragment = new StartFragment();
                                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
                            }
                        });
            }
        });
        return view;
    }
}
