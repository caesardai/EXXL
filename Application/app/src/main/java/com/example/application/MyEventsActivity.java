package com.example.application;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyEventsActivity extends AppCompatActivity {
    private MyEventsAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        String username = MainActivity.getUsername();

        // Initialize RecyclerView
        RecyclerView myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get user events from API. Show if there are events to show
        JSONObject[] jsonArray = getEventsFromAPI(username);
        if (jsonArray.length == 0){
            ConstraintLayout constraintLayout = findViewById(R.id.main_layout);
            View oldView = myRecyclerView;

            TextView newTV = new TextView(this);
            newTV.setId(View.generateViewId());
            newTV.setText("There are no events to show. Go join or create one!");
            newTV.setTextSize(20);

            constraintLayout.removeView(oldView);
            constraintLayout.addView(newTV, oldView.getLayoutParams());
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            constraintSet.connect(newTV.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, 15);
            constraintSet.connect(newTV.getId(), ConstraintSet.RIGHT, constraintLayout.getId(), ConstraintSet.RIGHT, 15);
            constraintSet.connect(newTV.getId(), ConstraintSet.TOP, R.id.appBarLayout, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(newTV.getId(), ConstraintSet.BOTTOM, constraintLayout.getId(), ConstraintSet.BOTTOM, 0);
            constraintSet.applyTo(constraintLayout);
        } else {
            myAdapter = new MyEventsAdapter(jsonArray, this);
            myRecyclerView.setAdapter(myAdapter);
        }

        // top app bar
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        MaterialToolbar topAppBar = appBarLayout.findViewById(R.id.topAppBar);
        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle back button clicks
                finish();
            }
        });

    }

    private JSONObject[] getEventsFromAPI(String username) {
        // modified from callEventsAPI() from MainActivity (thx xavier)
        // JSONArray to store events
        JSONArray jsonArray = new JSONArray();
        JSONObject[] objectArray = null;

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                        try {
                            URL url = new URL("http://10.0.2.2:3000/findEventsByUser?username=" + username);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();

                            // creates new JSONArray from response
                            JSONArray responseArray = new JSONArray(response);

                            // populates jsonArray (return array) from responseArray
                            for (int i = 0; i < responseArray.length(); i++) {
                                jsonArray.put(responseArray.getJSONObject(i));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
            executor.awaitTermination(2, TimeUnit.SECONDS);

            objectArray = new JSONObject[jsonArray.length()];
            for(int i = 0; i < jsonArray.length(); i++){
                objectArray[i] = jsonArray.getJSONObject(i);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return objectArray;
    }

    //
    public void leaveEvent(String eventID, View v){
        String username = MainActivity.getUsername();
        final String[] response = new String[1];

        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                        try{
                            URL url = new URL("http://10.0.2.2:3000/leaveEvent?username="
                                    + username + "&eventID=" + eventID);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Scanner in = new Scanner(url.openStream());
                            String response1 = in.nextLine();
                            response[0] = response1;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            );
            executor.awaitTermination(2, TimeUnit.SECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!response[0].equals("isHost")){
            finish();
            startActivity(getIntent());
        } else {
            Snackbar snackbar = Snackbar.make(v,
                    "You cannot leave if you are the host!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

}
