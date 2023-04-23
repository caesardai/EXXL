package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreateEvent extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    public void onCreateEventButtonClick(View view) {
        System.out.println("onCreateAccountButtonClick pressed");

        String name = ((EditText) findViewById(R.id.name)).getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Error: please specify event name", Toast.LENGTH_LONG).show();
            return;
        }

        String date = ((EditText) findViewById(R.id.date)).getText().toString().trim();
        if (date.isEmpty()) {
            Toast.makeText(this, "Error: please specify date", Toast.LENGTH_LONG).show();
            return;
        }

        String location = ((EditText) findViewById(R.id.location)).getText().toString().trim();
        if (location.isEmpty()) {
            Toast.makeText(this, "Error: please specify location", Toast.LENGTH_LONG).show();
            return;
        }

        String description = ((EditText) findViewById(R.id.description)).getText().toString().trim();

        // add to database
//        boolean successfulAdd = addEvent(name, date, location, description);
//        if (!successfulAdd) {
//            Toast.makeText(this, "Error creating account. Please try again later.", Toast.LENGTH_LONG).show();
//        } else {
//            // exit out of registration page
//            Intent toHome = new Intent(this, MainActivity.class);
//            toHome.putExtra("Message", "Registered!");
//            Toast.makeText(this, "Registered!", Toast.LENGTH_LONG).show();
//            startActivityForResult(toHome, 4);
//        }
    }

    /**
     * Adds an event to the database using Node
     * @return whether the event addition was successful (true or false)
     */
    private boolean addEvent(String name, String date, String location, String description) {
        boolean[] success = new boolean[1];
        System.out.println("Add Event");

        // creates new thread and connects to database running on local machine
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    URL url = new URL("http://10.0.2.2:3000/addEvent?name=" + name +
                            "&date=" + date +
                            "&location=" + location +
                            "&description=" + description +
                            "&host=" + "leoc");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    System.out.println(Boolean.parseBoolean(response));
                    success[0] = Boolean.parseBoolean(response);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });

            executor.awaitTermination(2, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return success[0];
    }
}

