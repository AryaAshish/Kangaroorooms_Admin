package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.architectica.kangaroorooms.theadminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VendorsList extends AppCompatActivity {

    DatabaseReference vendorsListReference;
    List<String> vendorNames;
    List<String> vendorEmails;
    List<String> vendorPhoneNumbers;
    List<String> vendorUids;
    UserAdapter vendorsAdapter;
    Toolbar toolbar;
    private TextView mToolText;
    RecyclerView vendorsListRecyclerView;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_list);

        mToolText = (TextView) findViewById(R.id.vendors_text);
        mToolText.setText("List of Landlords");
        toolbar = (Toolbar) findViewById(R.id.vendors_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        vendorEmails = new ArrayList<String>();
        vendorNames = new ArrayList<String>();
        vendorPhoneNumbers = new ArrayList<String>();
        vendorUids = new ArrayList<String>();

        vendorsListRecyclerView = (RecyclerView)findViewById(R.id.vendors_recycler_view);

        vendorsListReference = FirebaseDatabase.getInstance().getReference(CitiesList.cityName);

        pd = new ProgressDialog(VendorsList.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

       vendorsListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vendorEmails.clear();
                vendorNames.clear();
                vendorPhoneNumbers.clear();
                vendorUids.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                        DataSnapshot addressSnapshot = snapshot1.child("ParkingAddress");

                        for (DataSnapshot snapshot2 : addressSnapshot.getChildren()){

                            if (!vendorUids.contains(snapshot2.child("VendorUid").getValue(String.class))){

                                vendorUids.add(snapshot2.child("VendorUid").getValue(String.class));

                            }

                        }

                    }

                }

                DatabaseReference vendorReference = FirebaseDatabase.getInstance().getReference("Vendors/");

                vendorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (int i=0;i<vendorUids.size();i++){

                            DataSnapshot snapshot = dataSnapshot.child(vendorUids.get(i));

                            if (!vendorNames.contains(snapshot.child("Name").getValue(String.class))){
                                vendorNames.add(snapshot.child("Name").getValue(String.class));
                            }

                            if (!vendorEmails.contains(snapshot.child("Email").getValue(String.class))){
                                vendorEmails.add(snapshot.child("Email").getValue(String.class));
                            }

                            if (!vendorPhoneNumbers.contains(snapshot.child("PhoneNumber").getValue(String.class))){
                                vendorPhoneNumbers.add(snapshot.child("PhoneNumber").getValue(String.class));
                            }

                        }

                        pd.dismiss();

                        vendorsAdapter = new UserAdapter(VendorsList.this,vendorEmails,vendorNames,vendorPhoneNumbers,vendorUids);
                        vendorsListRecyclerView.setAdapter(vendorsAdapter);
                        vendorsListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        vendorsListRecyclerView.setMotionEventSplittingEnabled(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        pd.dismiss();

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
