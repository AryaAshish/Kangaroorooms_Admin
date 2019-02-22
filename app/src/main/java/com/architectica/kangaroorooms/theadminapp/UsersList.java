package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.architectica.kangaroorooms.theadminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersList extends AppCompatActivity {

    Toolbar toolbar;
    private TextView mToolText;
    RecyclerView customersListRecyclerView;
    List<String> customerNames;
    List<String> customerEmails;
    List<String> customerPhoneNumbers;
    List<String> customerUids;
    UserAdapter customersAdapter;
    DatabaseReference customersListReference;
    ProgressDialog pd;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {

            finish(); // close this activity and return to preview activity (if there is any)

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        mToolText = (TextView) findViewById(R.id.customers_text);
        mToolText.setText("List of Customers");
        toolbar = (Toolbar) findViewById(R.id.customers_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        customersListRecyclerView = (RecyclerView)findViewById(R.id.customers_recycler_view);
        customerEmails = new ArrayList<String>();
        customerNames = new ArrayList<String>();
        customerPhoneNumbers = new ArrayList<String>();
        customerUids = new ArrayList<String>();

        pd = new ProgressDialog(UsersList.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        customersListReference = FirebaseDatabase.getInstance().getReference("Users");

        customersListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                customerEmails.clear();
                customerNames.clear();
                customerPhoneNumbers.clear();
                customerUids.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    for (DataSnapshot snapshot1 : snapshot.child("Email").getChildren()){

                        customerEmails.add(snapshot1.getValue(String.class));

                    }

                    for (DataSnapshot snapshot2 : snapshot.child("PhoneNumber").getChildren()){

                        customerPhoneNumbers.add(snapshot2.getValue(String.class));

                    }

                    for (DataSnapshot snapshot3 : snapshot.child("Name").getChildren()){

                        customerNames.add(snapshot3.getValue(String.class));

                    }

                    customerUids.add(snapshot.getKey());

                }

                pd.dismiss();

                customersAdapter = new UserAdapter(UsersList.this,customerEmails,customerNames,customerPhoneNumbers,customerUids);
                customersListRecyclerView.setAdapter(customersAdapter);
                customersListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                customersListRecyclerView.setMotionEventSplittingEnabled(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

            }
        });

    }

}
