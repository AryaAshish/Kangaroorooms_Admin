package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodaysBookings extends AppCompatActivity {

    int redColorOn;
    private TextView mToolText;
    List<String> mCity;
    private Toolbar toolbar;
    RecyclerView mHotelsRecyclerView;
    HotelAdapter mHotelAdapter;
    private TextView mRoomOriginalCost;
    List<String> mVehicleNames;
    List<String> mVehicleLocations;
    List<String> mPricePerHour;
    List<String> mPricePerDay;
    List<String> mVehicleImages;
    List<String> VendorNames;
    List<Integer> noOfParkingAddresses;
    List<String> noOfVehicles;
    ArrayList<Integer> mDelete = new ArrayList<Integer>(100);
    ArrayList<Integer> mBlock = new ArrayList<Integer>(100);
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference,mReference;
    List<String> vehicleType;
    List<String> parkingAddresses;
    String image;
    String data;
    List<String> orderId,txnAmount,bankTxnId,txnId,bankName;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_bookings);

        MainActivity.content = "TodaysBookings";

        redColorOn = 0;
        Intent intent = getIntent();
        mVehicleNames = new ArrayList<String>();
        mVehicleLocations = new ArrayList<String>();
        mPricePerHour = new ArrayList<String>();
        mPricePerDay = new ArrayList<String>();
        mVehicleImages = new ArrayList<String>();
        VendorNames = new ArrayList<String>();
        orderId = new ArrayList<String>();
        txnAmount = new ArrayList<String>();
        bankTxnId = new ArrayList<String>();
        txnId = new ArrayList<String>();
        bankName = new ArrayList<String>();
        mCity = new ArrayList<String>();
        vehicleType = new ArrayList<String>();
        parkingAddresses = new ArrayList<String>();
        noOfParkingAddresses = new ArrayList<Integer>();
        noOfVehicles = new ArrayList<String>();

        UserAdapter.isBookings = true;

        pd = new ProgressDialog(TodaysBookings.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mRoomOriginalCost = (TextView) findViewById(R.id.pricePerHour) ;

        mHotelsRecyclerView = (RecyclerView) findViewById(R.id.hotels_recycler_view);

        mReference = mFirebaseDatabase.getReference("Bookings");

        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPricePerDay.clear();
                mPricePerHour.clear();
                mVehicleLocations.clear();
                mVehicleNames.clear();
                mVehicleImages.clear();
                mCity.clear();
                vehicleType.clear();
                parkingAddresses.clear();
                noOfParkingAddresses.clear();
                VendorNames.clear();
                mDelete.clear();
                orderId.clear();
                txnId.clear();
                bankName.clear();
                bankTxnId.clear();
                txnAmount.clear();
                mBlock.clear();
                noOfVehicles.clear();

                Calendar cal = Calendar.getInstance();

                for (DataSnapshot chidSnap : dataSnapshot.getChildren()){

                    if ((Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.get(Calendar.YEAR))).equals(chidSnap.child("BookingDate").getValue(String.class))){

                        mVehicleImages.add(chidSnap.child("VehiclePhoto").getValue(String.class));
                        mVehicleNames.add(chidSnap.child("VehicleName").getValue(String.class));
                        mPricePerHour.add(chidSnap.child("PricePerHour").getValue(String.class));
                        mPricePerDay.add(chidSnap.child("PricePerDay").getValue(String.class));
                        mCity.add(chidSnap.child("City").getValue(String.class));
                        vehicleType.add(chidSnap.child("VehicleType").getValue(String.class));
                        mVehicleLocations.add(chidSnap.child("ParkingAddress").getValue(String.class));
                        noOfVehicles.add(chidSnap.child("BookedNoOfVehicles").getValue(String.class));
                        VendorNames.add(chidSnap.child("Vendor").getValue(String.class));
                        mDelete.add(R.drawable.ic_delete_black_24dp);
                        mBlock.add(R.drawable.ic_block_black_24dp);

                        orderId.add(null);
                        txnAmount.add(null);
                        bankTxnId.add(null);
                        txnId.add(null);
                        bankName.add(null);

                    }

                }

                pd.dismiss();

                mHotelAdapter = new HotelAdapter(TodaysBookings.this, mVehicleImages, VendorNames, mCity, vehicleType, mVehicleNames,
                        mPricePerHour, mPricePerDay, mVehicleLocations, orderId, txnAmount, bankTxnId, txnId, bankName, mDelete, mBlock,noOfVehicles);
                mHotelsRecyclerView.setAdapter(mHotelAdapter);
                mHotelsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mHotelsRecyclerView.setMotionEventSplittingEnabled(false);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
            }
        });

    }
}

