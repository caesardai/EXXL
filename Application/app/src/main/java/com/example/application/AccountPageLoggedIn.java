package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AccountPageLoggedIn extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_page_logged_in);
    }

    public void onLogOutButtonClick(View view) {
        System.out.println("onLogOutButtonClick");
        MainActivity.setUsername(null);
        Intent in = new Intent(this, MainActivity.class);
        in.putExtra("Message", "we're out!");
        startActivityForResult(in, 300);
    }
}
