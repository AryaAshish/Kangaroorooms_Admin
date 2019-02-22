package com.architectica.kangaroorooms.theadminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.architectica.kangaroorooms.theadminapp.R;

public class UserListings extends AppCompatActivity {

    TextView nameTextView;
    TextView emailTextView;
    TextView phoneNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_listings);

        nameTextView = (TextView)findViewById(R.id.name);
        emailTextView = (TextView)findViewById(R.id.email);
        phoneNumberTextView = (TextView)findViewById(R.id.mobileNumber);

        nameTextView.setText(UserAdapter.userName);
        phoneNumberTextView.setText(UserAdapter.phoneNumber);
        emailTextView.setText(UserAdapter.userEmail);

    }
}
