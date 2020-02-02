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
    Button friendsRanking;
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
    ArrayList<Score> friendList = new ArrayList<>();
    ArrayList<Score> globalScores = new ArrayList<>();


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
        friendsRanking = view.findViewById(R.id.button);
        adapter = new RankingAdapter(getContext(), listOfGlobalScores);
//        if (firebaseAuth.getCurrentUser() != null) {
//            firebaseUser = firebaseAuth.getCurrentUser();
//            loginText.setVisibility(View.GONE);
//            if (!listOfGlobalScores.isEmpty()) {
//                listOfGlobalScores.clear();
//            } else {
//                prepareList();
//            }
//        }

        home.setOnClickListener(v -> {
            StartFragment startFragment = new StartFragment();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
        });

        loginText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        localRanking.setOnClickListener((view1) -> prepareLocalList());
        globalRanking.setOnClickListener(v -> prepareGlobalList());
        friendsRanking.setOnClickListener(v -> prepareFriendsList());
        rV.setLayoutManager(new LinearLayoutManager(getContext()));
        rV.setScrollbarFadingEnabled(false);
        rV.setAdapter(adapter);

        return view;
    }

    private void prepareGlobalList() {
        globalRanking.setBackgroundColor(Color.parseColor("#1A000000"));
        localRanking.setBackgroundColor(Color.TRANSPARENT);
        friendsRanking.setBackgroundColor(Color.TRANSPARENT);
        if (!checkInternetConnection()) {
            loginText.setText("No internet");
            progressBar.setVisibility(View.GONE);
            loginText.setVisibility(View.VISIBLE);
        } else {
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference("usrscr/");
            mFirebaseDatabase.keepSynced(false);
            mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    loginText.setVisibility(View.GONE);
                    globalScores.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot score : ds.getChildren()) {
                            Score score2 = new Score();
                            score2.setName(ds.getKey());
                            score2.setT(score.getValue(Score.class).getT());
                            score2.setS(score.getValue(Score.class).getS());
                            globalScores.add(score2);
                        }

                    }
                    sorting(globalScores);
                    for (int j = 24; j < globalScores.size(); j++) {
                        globalScores.remove(globalScores.get(j));
                    }
                    if (getActivity() != null) {
                        adapter = new RankingAdapter(getContext(), globalScores);
                    }
                    adapter.notifyDataSetChanged();
                    rV.setVisibility(View.VISIBLE);
                    rV.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    private void prepareFriendsList() {
        globalRanking.setBackgroundColor(Color.TRANSPARENT);
        localRanking.setBackgroundColor(Color.TRANSPARENT);
        friendsRanking.setBackgroundColor(Color.parseColor("#1A000000"));
        if (!checkInternetConnection()) {
            loginText.setText("No internet");
            progressBar.setVisibility(View.GONE);
            loginText.setVisibility(View.VISIBLE);
        } else {
            if (firebaseAuth.getCurrentUser() != null) {
                mFirebaseInstance = FirebaseDatabase.getInstance();
                mFirebaseDatabase = mFirebaseInstance.getReference("usrscr/");
                mFirebaseDatabase.keepSynced(false);
                mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        loginText.setVisibility(View.GONE);
                        friendList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String fullName = firebaseAuth.getCurrentUser().getEmail();
                            String username = fullName.split("@")[0];
                            //TODO implement friend list check
                            if (ds.getKey().equals(username)) {
                                for (DataSnapshot score : ds.getChildren()) {
                                    Score score2 = new Score();
                                    score2.setName(ds.getKey());
                                    score2.setT(score.getValue(Score.class).getT());
                                    score2.setS(score.getValue(Score.class).getS());
                                    friendList.add(score2);
                                }
                            }
                        }
                        sorting(friendList);
                        if (getActivity() != null) {
                            adapter = new RankingAdapter(getContext(), friendList);
                        }
                        adapter.notifyDataSetChanged();
                        rV.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                loginText.setVisibility(View.VISIBLE);
                rV.setVisibility(View.GONE);
            }
        }


    }

    private void prepareLocalList() {
        globalRanking.setBackgroundColor(Color.TRANSPARENT);
        localRanking.setBackgroundColor(Color.parseColor("#1A000000"));
        friendsRanking.setBackgroundColor(Color.TRANSPARENT);
        if (!checkInternetConnection()) {
            loginText.setText("No internet");
            progressBar.setVisibility(View.GONE);
            loginText.setVisibility(View.VISIBLE);
        } else {
            if (firebaseAuth.getCurrentUser() != null) {
                mFirebaseInstance = FirebaseDatabase.getInstance();
                mFirebaseDatabase = mFirebaseInstance.getReference("usrscr/");
                mFirebaseDatabase.keepSynced(false);
                mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        loginText.setVisibility(View.GONE);
                        listofLocalScores.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String fullName = firebaseAuth.getCurrentUser().getEmail();
                            String username = fullName.split("@")[0];
                            if (ds.getKey().equals(username)) {
                                for (DataSnapshot score : ds.getChildren()) {
                                    Score score2 = new Score();
                                    score2.setName(ds.getKey());
                                    score2.setT(score.getValue(Score.class).getT());
                                    score2.setS(score.getValue(Score.class).getS());
                                    listofLocalScores.add(score2);
                                }
                            }
                        }
                        if (listofLocalScores.isEmpty()) {
                            loginText.setText("Go and play!");
                            loginText.setVisibility(View.VISIBLE);
                        }
                        sorting(listofLocalScores);
                        adapter = new RankingAdapter(getContext(), listofLocalScores);
                        adapter.notifyDataSetChanged();
                        rV.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                loginText.setVisibility(View.VISIBLE);
                rV.setVisibility(View.GONE);
            }
        }
    }

    private void sorting(ArrayList<Score> unsortedList) {
        for (int i = 0; i < unsortedList.size(); i++) {
            for (int j = i + 1; j <= unsortedList.size() - 1; j++) {
                if (unsortedList.get(i).getS() < unsortedList.get(j).getS()) {
                    Collections.swap(unsortedList, j, i);
                }
            }
        }
        for (int i = 0; i < unsortedList.size(); i++) {
            for (int j = i + 1; j <= unsortedList.size() - 1; j++) {
                if (unsortedList.get(i).getS() == unsortedList.get(j).getS()) {
                    sortTime(unsortedList, unsortedList.get(i), unsortedList.get(j));
                }
            }
        }
    }

    private void sortTime(ArrayList<Score> sortingForMinutes, Score o1, Score o2) {
        if (o1.getT() > o2.getT()) {
            Collections.swap(sortingForMinutes, sortingForMinutes.indexOf(o2), sortingForMinutes.indexOf(o1));
        }
    }

    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }


}
