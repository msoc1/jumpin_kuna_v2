package com.fixed4fun.jumpingkunav2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> yourFriends = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StartFragment startFragment = new StartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String json2 = sharedPrefs.getString("FRIENDS", "");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json2, type);
        yourFriends.clear();
        if (arrayList != null) {
            yourFriends.addAll(arrayList);
        }
    }

    @Override
    public void onBackPressed() {
        StartFragment startFragment = new StartFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.constraint_main, startFragment).commit();
        StartFragment.jumpInBackground.purge();
        StartFragment.jumpInBackground.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        editor.remove("FRIENDS");
        String json = gson.toJson(yourFriends);
        editor.putString("FRIENDS", json);
        editor.apply();
    }

}

