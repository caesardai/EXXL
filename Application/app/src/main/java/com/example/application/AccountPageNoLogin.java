package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AccountPageNoLogin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_page_no_login);
    }

    public void onLoginButtonClick(View view) {
        System.out.println("TODO: go to login page");
    }

    public void onNewAccountButtonClick(View view) {
        System.out.println("onNewAccountButtonClick");
        Intent in = new Intent(this, CreateAccount.class);
        in.putExtra("Message", "random message; new account time");
        startActivityForResult(in, 1);
    }
}
