package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity{

    private static final int ACTIVITY_ID = 1;
    protected String message;
    protected boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load events from database
        loadEvents();

        System.out.println("Hi");
        // set up nav bar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_1:
                        // go to other screen
                        System.out.println("hello!");
                        return true;

                    case R.id.bottom_nav_2:
                        return true;

                    case R.id.bottom_nav_3:
                        return true;

                    case R.id.bottom_nav_4:
                        // go to account page
//                        if (!loggedIn) {
//                            Intent in = new Intent(this, AccountPageNoLogin.class);
//                            in.putExtra("Message", "random message; hello");
//                            startActivityForResult(in, ACTIVITY_ID);
//                        }
                        return true;
                }
                return false;
            }
        });

    }


    /**
     * populates the ListView component with events from the database
     */
    public void loadEvents() {

        // create JSONArray with response from events endpoint
        JSONArray jsonArray = callEventsAPI();

        // find created ListView
        ListView listView = (ListView) findViewById(R.id.event_list);

        // new ArrayList to store events and map to the ListView
        ArrayList<Event> arrayList = new ArrayList<Event>();

        try {

            // add each event to arrayList
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currObject = (JSONObject) jsonArray.get(i);
                Event currEvent = new Event(currObject);
                arrayList.add(currEvent);
            }
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
        }

        // map arrayList (storing events) to the ListView
        ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(this,
                android.R.layout.simple_selectable_list_item, arrayList);
        listView.setAdapter(adapter);

        // create listener for onClick in the Listview
        // THIS DOESN'T DO ANYTHING YET - FOR OPENING EVENTS (next iteration)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);
            }
        });
    }

    /**
     * calls events endpoint to retrieve events data
     * @return JSONArray of all the events in the database
     */
    private JSONArray callEventsAPI() {

        // JSONArray to store events
        JSONArray jsonArray = new JSONArray();

        // code from Chris's sample app
        // creates new thread and connects to database running on local machine
        // calls findEvents endpoint
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                        try {
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"
                            URL url = new URL("http://10.0.2.2:3000/findEvents");

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
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            message = e.toString();
                        }
                    }
            );

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

        }
        catch (Exception e) {
            // uh oh
            e.printStackTrace();
        }
        return jsonArray;
    }

}