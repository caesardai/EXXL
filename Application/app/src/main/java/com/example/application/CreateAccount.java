package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    public void onCreateAccountButtonClick(View view) {
        boolean validInput = true;
        System.out.println("onCreateAccountButtonClick pressed");
        String first_name = ((EditText) findViewById(R.id.first_name)).getText().toString().trim();
        System.out.println(first_name); // look under Run for print statements
        String last_name = ((EditText) findViewById(R.id.last_name)).getText().toString().trim();

        String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();

        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String repassword = ((EditText) findViewById(R.id.repassword)).getText().toString();

        if (password.isEmpty() || repassword.isEmpty()) {
            // invalid password Toast
            return;
        }

        if (password.equals(repassword)) {
            // invalid password Toast
            return;
        }

        if (validInput) {
            // add to database
            // exit out of registration page
        }
    }
}
