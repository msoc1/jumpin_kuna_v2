package com.fixed4fun.jumpingkunav2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.fixed4fun.jumpingkunav2.MainActivity.yourFriends;

public class CollisionDetection {

    public CollisionDetection() {
    }

    public boolean collision(ImageView a, View b) {
        float firstObjectLeftTopX, firstObjectLeftBottomX, firstObjectRightTopX, firstObjectRightBottomX;
        float firstObjectLeftTopY, firstObjectLeftBottomY, firstObjectRightTopY, firstObjectRightBottomY;

        float secondObjectLeftTopX, secondObjectLeftBottomX, secondObjectRightTopX, secondObjectRightBottomX;
        float secondObjectLeftTopY, secondObjectLeftBottomY, secondObjectRightTopY, secondObjectRightBottomY;

        float firObjGetX, firObjGetY, firObjHeight, firObjWidth;
        float secObjGetX, secObjGetY, secObjHeight, secObjWidth;

        int iceObstacleMargin = 15;


        firObjGetX = a.getX();
        firObjGetY = a.getY();
        firObjHeight = a.getHeight();
        firObjWidth = a.getWidth();

        secObjGetX = b.getX();
        secObjGetY = b.getY();
        secObjHeight = b.getHeight();
        secObjWidth = b.getWidth();


        firstObjectLeftTopX = firObjGetX + 5;
        firstObjectLeftTopY = firObjGetY - 5;
        firstObjectLeftBottomX = firObjGetX + 5;
        firstObjectLeftBottomY = firObjGetY + firObjHeight - 5;
        firstObjectRightTopX = firObjGetX + firObjWidth - 5;
        firstObjectRightTopY = firObjGetY + 5;
        firstObjectRightBottomX = firObjGetX + firObjWidth - 5;
        firstObjectRightBottomY = firObjGetY + firObjHeight - 5;


        secondObjectLeftTopX = secObjGetX + iceObstacleMargin;
        secondObjectLeftTopY = secObjGetY + iceObstacleMargin;
        secondObjectLeftBottomX = secObjGetX + iceObstacleMargin;
        secondObjectLeftBottomY = secObjGetY + secObjHeight * 0.8f - 5;
        secondObjectRightTopX = secObjGetX + secObjWidth - iceObstacleMargin;
        secondObjectRightTopY = secObjGetY + iceObstacleMargin;
        secondObjectRightBottomX = secObjGetX + secObjWidth - iceObstacleMargin;
        secondObjectRightBottomY = secObjGetY + secObjHeight - iceObstacleMargin;

        //check for top bottom corner
        if (firstObjectLeftBottomY >= secondObjectLeftTopY && firstObjectLeftBottomY <= secondObjectLeftBottomY) {
            if (firstObjectLeftBottomX >= secondObjectLeftTopX && firstObjectLeftBottomX <= secondObjectRightTopX) {
                return false;
            }
        }

        //check for middle of right X edge
        if (firstObjectLeftBottomY - (firObjHeight / 2) >= secondObjectLeftTopY && firstObjectLeftBottomY - (firObjHeight / 2) <= secondObjectLeftBottomY) {
            if (firstObjectLeftBottomX >= secondObjectLeftTopX && firstObjectLeftBottomX <= secondObjectRightTopX) {
                return true;
            }
        }

        //check for right bottom corner
        if (firstObjectRightBottomY >= secondObjectLeftTopY && firstObjectRightBottomY <= secondObjectLeftBottomY) {
            if (firstObjectRightBottomX >= secondObjectLeftTopX && firstObjectRightBottomX <= secondObjectRightTopX) {
                return true;
            }
        }

        //check for top left  corner
        if (firstObjectLeftTopY <= secondObjectLeftBottomY && firstObjectLeftTopY >= secondObjectRightTopY) {
            if (firstObjectLeftTopX >= secondObjectLeftBottomX && firstObjectLeftTopX <= secondObjectRightBottomX) {
                return true;
            }
        }

        //check for right top corner
        if ((firstObjectRightTopY <= secondObjectLeftBottomY && firstObjectRightTopY >= secondObjectRightTopY)) {
            if (firstObjectRightTopX >= secondObjectLeftBottomX && firstObjectRightTopX <= secondObjectRightBottomX) {
                return true;
            }
        }

        //check for middle of right X edge
        if ((firstObjectRightTopY + firObjHeight / 2 <= secondObjectLeftBottomY && firstObjectRightTopY + firObjHeight / 2 >= secondObjectRightTopY)) {
            if (firstObjectRightTopX >= secondObjectLeftBottomX && firstObjectRightTopX <= secondObjectRightBottomX) {
                return true;
            }
        }


        return false;
    }

    /**
     * A simple {@link Fragment} subclass.
     */
    public static class FriendsFragment extends Fragment {


        public FriendsFragment() {
            // Required empty public constructor
        }

        Button home;
        Button addFriend;
        FriendsAdapter rankingAdapter;
        RecyclerView friendsRecyclerView;
        private FirebaseAuth firebaseAuth;
        EditText friendName;
        private FirebaseDatabase mFirebaseInstance;
        private DatabaseReference mFirebaseDatabase;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_friends, container, false);
            home = view.findViewById(R.id.home_from_friends);
            friendsRecyclerView = view.findViewById(R.id.friend_recyclerview);
            friendName = view.findViewById(R.id.search_friend);
            addFriend = view.findViewById(R.id.add_friend);

            home.setOnClickListener(v -> {
                StartFragment startFragment = new StartFragment();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
            });
            rankingAdapter = new FriendsAdapter(getContext(), yourFriends);
            friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            friendsRecyclerView.setAdapter(rankingAdapter);

            rankingAdapter.setOnItemClickListener(position -> {
                yourFriends.remove(position);
                rankingAdapter.notifyDataSetChanged();
            });

            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference("users");

            addFriend.setOnClickListener(v -> {
                if (!friendName.getText().toString().isEmpty()) {
                    mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                if ((friendName.getText().toString() + "@jumpinkuna.pl").equals(dataSnapshot1.getValue())) {
                                    String username = dataSnapshot1.getValue().toString().split("@")[0];
                                    yourFriends.add(username);
                                    rankingAdapter.notifyDataSetChanged();
                                    friendName.setText(null);
                                    Toast.makeText(getContext(), "User added", Toast.LENGTH_SHORT).show();
                                    break;
                                } else {
                                    Toast.makeText(getContext(), "User doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(getContext(), "Enter user", Toast.LENGTH_SHORT).show();
                }
            });
            firebaseAuth = FirebaseAuth.getInstance();
            return view;
        }


    }
}
