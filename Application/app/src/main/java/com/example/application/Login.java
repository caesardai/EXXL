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

public class Login extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    public void onSignInButtonClick(View view) {
        System.out.println("onSignInButtonClick pressed");

        String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
        if (username.isEmpty()) {
            Toast.makeText(this, "Please specify username.", Toast.LENGTH_LONG).show();
            return;
        }

        String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
        if (password.isEmpty()) {
            Toast.makeText(this, "Please specify password.", Toast.LENGTH_LONG).show();
            return;
        }

        // If username is not in database
        if (!CreateAccount.checkUsername(username)) {
            Toast.makeText(this, "Username not in system.\nTry again or create an account.", Toast.LENGTH_LONG).show();
            return;
        } else {
            if (validatePassword(username, password)) {
                // exit out of login page
                Intent toHome = new Intent(this, MainActivity.class);
                toHome.putExtra("Message", "Logged in!");
                Toast.makeText(this, "Logged in!", Toast.LENGTH_SHORT).show();
                MainActivity.setUsername(username);
                startActivityForResult(toHome, 6);
            } else {
                Toast.makeText(this, "Username and password do not match. Try again.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onNewAccountButtonClick(View view) {
        System.out.println("onNewAccountButtonClick");
        Intent in = new Intent(this, CreateAccount.class);
        in.putExtra("Message", "random message; new account time");
        startActivityForResult(in, 1);
    }

    /**
     * @return true if the password and username match; false otherwise
     */
    private boolean validatePassword(String username, String password) {
        boolean[] inDatabase = new boolean[1];
        System.out.println("validatePassword");
        System.out.println(username + " " + password);

        // creates new thread and connects to database running on local machine
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute( () -> {
                try {
                    // assumes that there is a server running on the AVD's host on port 3000
                    URL url = new URL("http://10.0.2.2:3000/validatePassword?username=" + username +
                            "&password=" + password);
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

            // waits for up to 2 seconds; not truly asynchronous but OK for our purposes
            executor.awaitTermination(2, TimeUnit.SECONDS);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return inDatabase[0];
    }
}
