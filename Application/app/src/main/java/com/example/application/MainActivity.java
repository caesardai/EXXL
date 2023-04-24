package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.EventLog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_ID = 1;
    protected String message;
    private static String username;

    protected BottomSheetDialog bottomSheetDialog;

    protected boolean seenBottomSheetDialog = false;

    // new ArrayList to store events and map to the ListView
    protected ArrayList<EventObject> eventsArrayList = new ArrayList<EventObject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load events from database
        loadEvents();

        // if logged in, show add event button
       if (username != null)
            findViewById(R.id.newEventFAB).setVisibility(View.VISIBLE);

        // set up nav bar
        NavigationBarView navigationBarView = findViewById(R.id.bottom_navigation);
        Intent navAccountSignedOut = new Intent(this, AccountPageNoLogin.class);
        Intent navAccountSignedIn = new Intent(this, AccountPageLoggedIn.class);
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
                        if (username == null) {
                            navAccountSignedOut.putExtra("Message", "random message; hello");
                            startActivityForResult(navAccountSignedOut, ACTIVITY_ID);
                        } else {
                            navAccountSignedIn.putExtra("Message", "random message; hello");
                            startActivityForResult(navAccountSignedIn, 500);
                        }
                        return true;
                }
                return false;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newEventFAB);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), CreateEvent.class);
                startActivity(i);
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

        try {

            // add each event to arrayList
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currObject = (JSONObject) jsonArray.get(i);
                EventObject currEvent = new EventObject(currObject);
                eventsArrayList.add(currEvent);
            }
        }
        catch (JSONException e) {
            // uh oh
            e.printStackTrace();
        }

        setEventAdapter();

        Intent viewEvent = new Intent(this, ViewSingleEvent.class);

        // create listener for onClick in the Listview, opens a single event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                EventObject eventObject = eventsArrayList.get(position);
                String eventId = eventObject.getAttribute("_id");
                view.setSelected(true);
                viewEvent.putExtra("eventId", eventId);
                viewEvent.putExtra("username", username);
                startActivityForResult(viewEvent, (int) id);
            }
        });
    }

    private void setEventAdapter() {

        // find created ListView
        ListView listView = (ListView) findViewById(R.id.event_list);

        // map arrayList (storing events) to the ListView
        ArrayAdapter<EventObject> adapter = new ArrayAdapter<EventObject>(this,
                android.R.layout.simple_selectable_list_item, eventsArrayList);
        listView.setAdapter(adapter);
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

    public static void setUsername(String username) {
        MainActivity.username = username;
    }

    public static String getUsername() {
        return MainActivity.username;
    }
    public void filterEvents(MenuItem item) {
        showBottomSheetDialog();
    }

    private void showBottomSheetDialog() {

        if (!seenBottomSheetDialog) {
            bottomSheetDialog = new BottomSheetDialog(this);
            seenBottomSheetDialog = true;
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);
        }

        bottomSheetDialog.show();

        ListView listView = bottomSheetDialog.findViewById(R.id.filter_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                listView.setItemChecked(position, true);
                switch (position) {
                    case 0:
                        filterByDate(); break;
                    case 1:
                        filterByName(); break;
                    case 2:
                        filterByLocation(); break;
                }
                bottomSheetDialog.dismiss();
            }
        });
    }

    private void filterByDate(){
        eventsArrayList.sort(new DateSorter());
        Collections.reverse(eventsArrayList);
        setEventAdapter();
    }

    private void filterByName(){
        eventsArrayList.sort(new NameSorter());
        setEventAdapter();
    }

    private void filterByLocation(){
        eventsArrayList.sort(new LocationSorter());
        setEventAdapter();
    }

}