package com.example.application;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyEventsActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private MyEventsAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        String username = MainActivity.getUsername();

        // Navigation bar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.bottom_nav_3);

        // Initialize RecyclerView
        RecyclerView myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // get user events from API
        JSONObject[] jsonArray = getEventsFromAPI(username);
        myAdapter = new MyEventsAdapter(jsonArray);
        myRecyclerView.setAdapter(myAdapter);

        System.out.println("Testing1");
        AppBarLayout appBarLayout = findViewById(R.id.appBarLayout);
        MaterialToolbar topAppBar = appBarLayout.findViewById(R.id.topAppBar);
        System.out.println("Testing2");



//        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // handle back button clicks
//                finish();
//            }
//        });


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
                            // assumes that there is a server running on the AVD's host on port 3000
                            URL url = new URL("http://10.0.2.2:3000/findEvents");
                            //URL url = new URL("http://10.0.2.2:3000/findEventsByUser?username=" + username);

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

}
