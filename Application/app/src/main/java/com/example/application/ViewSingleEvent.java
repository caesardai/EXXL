package com.example.application;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ViewSingleEvent extends AppCompatActivity {

    protected String message;

    protected String eventId;
    protected String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_single_event);

        eventId = getIntent().getStringExtra("eventId");
        userId = getIntent().getStringExtra("userId");

        EventObject eventObject = callSingleEventAPI(eventId);

        Log.i("made it to end", eventObject.toString());

        TextView eventTitle = findViewById(R.id.event_title);
        eventTitle.setText(eventObject.getAttribute("name"));

        TextView eventDate = findViewById(R.id.event_date);
        eventDate.setText(eventObject.getAttribute("date"));

        TextView eventLocation = findViewById(R.id.event_location);
        eventLocation.setText(eventObject.getAttribute("location"));

        TextView eventDescription = findViewById(R.id.event_description);
        eventDescription.setText(eventObject.getAttribute("description"));

    }

    private EventObject callSingleEventAPI(String eventId) {

        EventObject eventObject = new EventObject();

        // code from Chris's sample app
        // creates new thread and connects to database running on local machine
        // calls findSingleEvent endpoint
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                        try {

                            String urlString = "http://10.0.2.2:3000/findSingleEvent?id=" + eventId;
                            // assumes that there is a server running on the AVD's host on port 3000
                            // and that it has a /test endpoint that returns a JSON object with
                            // a field called "message"
                            URL url = new URL(urlString);

                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.connect();

                            Scanner in = new Scanner(url.openStream());
                            String response = in.nextLine();

                            // creates new JSONArray from response
                            JSONArray responseArray = new JSONArray(response);

                            // JSONArray contains one event (the event we want), so get that
                            JSONObject responseObject  = responseArray.getJSONObject(0);

                            eventObject.setObject(responseObject);
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
        return eventObject;
    }

    public void onJoinEvent(View v) {

        if (userId.equals("")) {
            Toast.makeText(this, "Please log in to join an event", Toast.LENGTH_LONG).show();
        }
        else {
            // code from Chris's sample app
            // creates new thread and connects to database running on local machine
            try {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute( () -> {
                            try {

                                String urlString = "http://10.0.2.2:3000/addUserEvent?eventId=" + eventId + "&userId=" + userId;
                                // assumes that there is a server running on the AVD's host on port 3000
                                // and that it has a /test endpoint that returns a JSON object with
                                // a field called "message"
                                URL url = new URL(urlString);

                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.connect();

                                Scanner in = new Scanner(url.openStream());
                                String response = in.nextLine();


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
        }


    }

}