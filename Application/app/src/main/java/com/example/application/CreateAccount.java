package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreateAccount extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    public void onCreateAccountButtonClick(View view) {
        System.out.println("onCreateAccountButtonClick pressed");

        String firstName = ((EditText) findViewById(R.id.first_name)).getText().toString().trim();
        if (firstName.isEmpty()) {
            Toast.makeText(this, "Error: please specify first name", Toast.LENGTH_LONG).show();
            return;
        }
        System.out.println(firstName); // look under Run for print statements

        String lastName = ((EditText) findViewById(R.id.last_name)).getText().toString().trim();
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Error: please specify last name", Toast.LENGTH_LONG).show();
            return;
        }

        String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(this, "Error: please specify username", Toast.LENGTH_LONG).show();
            return;
        }

        // test if username is already in database
        if (checkUsername(username)) {
            Toast.makeText(this, "Username already taken. Choose another.", Toast.LENGTH_LONG).show();
            return;
        }

        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String repassword = ((EditText) findViewById(R.id.repassword)).getText().toString();

        if (password.isEmpty()) {
            Toast.makeText(this, "Error: please specify password", Toast.LENGTH_LONG).show();
            return;
        }

        if (repassword.isEmpty()) {
            Toast.makeText(this, "Error: please confirm password", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(repassword)) {
            Toast.makeText(this, "Error: passwords do not match", Toast.LENGTH_LONG).show();
            return;
        }

        System.out.println("password = " + password + "; repassword = " + repassword);

        // add to database
        boolean successfulAdd = addUser(firstName, lastName, username, password);
        if (!successfulAdd) {
            Toast.makeText(this, "Error creating account. Please try again later.", Toast.LENGTH_LONG).show();
        } else {
            // exit out of registration page
            Intent toHome = new Intent(this, AccountPageLoggedIn.class);
            MainActivity.setUsername(username);
            toHome.putExtra("User", username);
            Toast.makeText(this, "Registered!", Toast.LENGTH_LONG).show();
            startActivityForResult(toHome, 4);
        }
    }

    public void onLoginButtonClick(View view) {
        System.out.println("onLoginButtonClick");
        Intent in = new Intent(this, Login.class);
        in.putExtra("Message", "random message; login time");
        startActivityForResult(in, 3);
    }

    /**
     *
     */
    static boolean checkUsername(String username) {
        boolean[] inDatabase = new boolean[1];

        // creates new thread and connects to database running on local machine
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    URL url = new URL("http://10.0.2.2:3000/checkForUsername?username=" + username);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();

                    Scanner in = new Scanner(url.openStream());
                    String response = in.nextLine();

                    System.out.println(Boolean.parseBoolean(response));
                    inDatabase[0] = Boolean.parseBoolean(response);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            });

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return inDatabase[0];
    }

    /**
     * @return whether the addition was successful
     */
    private boolean addUser(String firstName, String lastName, String username, String password) {
        boolean[] success = new boolean[1];
        System.out.println("Add User");
        System.out.println(firstName + " " + lastName + " " + username + " " + password);

        // creates new thread and connects to database running on local machine
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    URL url = new URL("http://10.0.2.2:3000/addUser?firstName=" + firstName +
                            "&lastName=" + lastName +
                            "&username=" + username +
                            "&password=" + password);
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

            // this waits for up to 2 seconds
            // it's a bit of a hack because it's not truly asynchronous
            // but it should be okay for our purposes (and is a lot easier)
            executor.awaitTermination(2, TimeUnit.SECONDS);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return success[0];
    }
}

