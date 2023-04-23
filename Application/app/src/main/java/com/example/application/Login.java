package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
            Toast.makeText(this, "Please specify username", Toast.LENGTH_LONG).show();
            return;
        }

        String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
        if (password.isEmpty()) {
            Toast.makeText(this, "Please specify password", Toast.LENGTH_LONG).show();
            return;
        }

        // If username is not in database
        if (!CreateAccount.checkUsername(username)) {
            Toast.makeText(this, "Username not in system.\nTry again or create an account.", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void onNewAccountButtonClick(View view) {
        System.out.println("onNewAccountButtonClick");
        Intent in = new Intent(this, CreateAccount.class);
        in.putExtra("Message", "random message; new account time");
        startActivityForResult(in, 1);
    }
}
