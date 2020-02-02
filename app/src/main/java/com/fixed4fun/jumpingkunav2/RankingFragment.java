package com.fixed4fun.jumpingkunav2;


import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {


    public RankingFragment() {
        // Required empty public constructor
    }

    Button home;
    Button localRanking;
    Button globalRanking;
    RecyclerView rV;
    TextView loginText;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    RankingAdapter adapter;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    ArrayList<Score> listOfGlobalScores = new ArrayList<>();
    ArrayList<Score> listofLocalScores = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        container.removeAllViews();
        home = view.findViewById(R.id.home_from_ranking);


        progressBar = view.findViewById(R.id.progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();
        rV = view.findViewById(R.id.ranking_recyclerView);
        loginText = view.findViewById(R.id.log_in_textview);
        localRanking = view.findViewById(R.id.local_ranking);
        globalRanking = view.findViewById(R.id.global_ranking);
        adapter = new RankingAdapter(getContext(), listOfGlobalScores);
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            loginText.setVisibility(View.GONE);
            if (!listOfGlobalScores.isEmpty()) {
                listOfGlobalScores.clear();
            } else {
                prepareList();
            }
        }
        globalRanking.setBackgroundColor(Color.parseColor("#1A000000"));

        home.setOnClickListener(v -> {
            StartFragment startFragment = new StartFragment();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
        });

        localRanking.setOnClickListener((view1) -> localRankingOnClick());
        globalRanking.setOnClickListener(v -> globalRankingOnclick());
        rV.setLayoutManager(new LinearLayoutManager(getContext()));
        rV.setScrollbarFadingEnabled(false);
        rV.setAdapter(adapter);

        return view;
    }

    public void prepareList() {
        if (!checkInternetConnection()) {
            loginText.setText("No internet");
            progressBar.setVisibility(View.GONE);
            loginText.setVisibility(View.VISIBLE);
        } else {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference("scores/");
            mFirebaseDatabase.keepSynced(true);
            mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    loginText.setVisibility(View.GONE);
                    listOfGlobalScores.clear();
                    listofLocalScores.clear();
                    addData(dataSnapshot);
                    adapter.notifyDataSetChanged();
                    sorting(listOfGlobalScores);
                    sorting(listofLocalScores);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sorting(ArrayList<Score> unsortedList) {
        for (int i = 0; i < unsortedList.size(); i++) {
            for (int j = i + 1; j <= unsortedList.size() - 1; j++) {
                if (unsortedList.get(i).getScore() < unsortedList.get(j).getScore()) {
                    Collections.swap(unsortedList, j, i);
                }
            }
        }
        for (int i = 0; i < unsortedList.size(); i++) {
            for (int j = i + 1; j <= unsortedList.size() - 1; j++) {
                if (unsortedList.get(i).getScore() == unsortedList.get(j).getScore()) {
                    sortTime(unsortedList, unsortedList.get(i), unsortedList.get(j));
                }
            }
        }
    }

    private void sortTime(ArrayList<Score> sortingForMinutes, Score o1, Score o2) {
        if (o1.getTime() > o2.getTime()) {
            Collections.swap(sortingForMinutes, sortingForMinutes.indexOf(o2), sortingForMinutes.indexOf(o1));
        }
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }


    private void addData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Score score = new Score();
            score.setName(ds.getValue(Score.class).getName());
            score.setTime(ds.getValue(Score.class).getTime());
            score.setScore(ds.getValue(Score.class).getScore());
            if (firebaseUser != null) {
                if (firebaseUser.getEmail().equals((ds.getValue(Score.class).getName()) + "@jumpinkuna.pl")) {
                    listofLocalScores.add(score);
                }
            }
            listOfGlobalScores.add(score);
        }
    }

    private void globalRankingOnclick() {
        loginText.setVisibility(View.GONE);
        if (listOfGlobalScores.isEmpty()) {
            if (!checkInternetConnection()) {
                loginText.setText("No internet");
                progressBar.setVisibility(View.GONE);
                loginText.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
        }
        localRanking.setBackgroundColor(Color.TRANSPARENT);
        globalRanking.setBackgroundColor(Color.parseColor("#1A000000"));
        adapter = new RankingAdapter(getContext(), listOfGlobalScores);
        rV.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        rV.setVisibility(View.VISIBLE);
    }

    private void localRankingOnClick() {
        globalRanking.setBackgroundColor(Color.TRANSPARENT);
        localRanking.setBackgroundColor(Color.parseColor("#1A000000"));
        if (firebaseAuth.getCurrentUser() != null) {
            //User logged in, we can populate both local and global ranking, default is global ranking
            progressBar.setVisibility(View.VISIBLE);
            firebaseUser = firebaseAuth.getCurrentUser();
            loginText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            adapter = new RankingAdapter(getContext(), listofLocalScores);
            rV.setAdapter(adapter);
            if (listofLocalScores.isEmpty()) {
                if (checkInternetConnection()) {
                    loginText.setText("Go and play!");
                } else {
                    loginText.setText("No internet");
                }
                loginText.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);

        } else {
            loginText.setVisibility(View.VISIBLE);
            rV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loginText.setVisibility(View.GONE);
        if (!listOfGlobalScores.isEmpty()) {
            listOfGlobalScores.clear();
        } else {
            prepareList();
        }

    }
}
