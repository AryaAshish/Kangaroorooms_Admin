package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CouponsActivity extends AppCompatActivity {

    List<String> couponCodes;
    List<String> couponDescriptions;
    List<String> applicableTypes;
    List<String> applicableNames;
    List<String> maxApplicableTimes;
    CouponAdapter couponAdapter;
    RecyclerView couponsRecyclerView;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons);

        couponCodes = new ArrayList<String>();
        couponDescriptions = new ArrayList<String>();
        applicableTypes = new ArrayList<String>();
        applicableNames = new ArrayList<String>();
        maxApplicableTimes = new ArrayList<String>();

        pd = new ProgressDialog(CouponsActivity.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        couponsRecyclerView = (RecyclerView)findViewById(R.id.couponsRecyclerView);

        DatabaseReference couponReference = FirebaseDatabase.getInstance().getReference();

        couponReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                couponCodes.clear();
                couponDescriptions.clear();
                applicableTypes.clear();
                applicableNames.clear();
                maxApplicableTimes.clear();

                if (dataSnapshot.hasChild("Coupons")){

                    DataSnapshot snapshot = dataSnapshot.child("Coupons");

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                        couponCodes.add(snapshot1.getKey());
                        couponDescriptions.add(snapshot1.child("Description").getValue(String.class));
                        applicableTypes.add(snapshot1.child("VehicleType").getValue(String.class));
                        applicableNames.add(snapshot1.child("VehicleName").getValue(String.class));
                        maxApplicableTimes.add(snapshot1.child("MaxTimes").getValue(String.class));

                    }

                }

                pd.dismiss();

                couponAdapter = new CouponAdapter(CouponsActivity.this,couponCodes,couponDescriptions,applicableTypes,applicableNames,maxApplicableTimes);
                couponsRecyclerView.setAdapter(couponAdapter);
                couponsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                couponsRecyclerView.setMotionEventSplittingEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

