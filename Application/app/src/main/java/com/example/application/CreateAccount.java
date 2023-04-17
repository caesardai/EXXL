package com.example.application;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccount  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
    }

    public void onCreateButtonClick(View view) {
        String first_name = ((EditText) findViewById(R.id.first_name)).getText().toString().trim();
        String last_name = ((EditText) findViewById(R.id.last_name)).getText().toString().trim();
        String username = ((EditText) findViewById(R.id.username)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.password)).getText().toString().trim();
    }
}
