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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VerifyRooms extends AppCompatActivity {

    RecyclerView vehiclesRecyclerView;
    Toolbar toolbar;
    private TextView mToolText;
    List<String> mVehicleNames;
    List<String> mVehicleLocations;
    List<String> mPricePerHour;
    List<String> mPricePerDay;
    List<String> mVehicleImages;
    List<String> VendorNames;
    ArrayList<Integer> mDelete = new ArrayList<Integer>(100);
    ArrayList<Integer> mBlock = new ArrayList<Integer>(100);
    List<String> vehicleType;
    List<String> orderId,txnAmount,bankTxnId,txnId,bankName;
    List<String> mCity;
    static List<String> roomUids;
    List<String> noOfVehicles;
    ProgressDialog pd;
    HotelAdapter mHotelAdapter;
    DatabaseReference vehiclesReference;

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
        setContentView(R.layout.activity_verify_rooms);

        MainActivity.content = "Verify Rooms";

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
        roomUids = new ArrayList<String>();
        UserAdapter.isBookings = false;
        noOfVehicles = new ArrayList<String>();
        VehiclesList.vendorUids = new ArrayList<String>();

        mToolText = (TextView) findViewById(R.id.tool_text);
        mToolText.setText("Verify Rooms");
        toolbar = (Toolbar) findViewById(R.id.activity_hotels_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        vehiclesRecyclerView = (RecyclerView)findViewById(R.id.hotels_recycler_view);

        vehiclesReference = FirebaseDatabase.getInstance().getReference(CitiesList.cityName);

        pd = new ProgressDialog(VerifyRooms.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        assignValuesFromFirebase(vehiclesReference);

    }

    public void assignValuesFromFirebase(DatabaseReference databaseReference){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPricePerDay.clear();
                mPricePerHour.clear();
                mVehicleLocations.clear();
                mVehicleNames.clear();
                mVehicleImages.clear();
                mCity.clear();
                vehicleType.clear();
                roomUids.clear();
                //parkingAddresses.clear();
                //noOfParkingAddresses.clear();
                VendorNames.clear();
                mDelete.clear();
                orderId.clear();
                txnId.clear();
                bankName.clear();
                bankTxnId.clear();
                txnAmount.clear();
                mBlock.clear();
                noOfVehicles.clear();
                VehiclesList.vendorUids.clear();

                for (DataSnapshot chidSnap : dataSnapshot.getChildren()) {

                    for (DataSnapshot snapshot : chidSnap.getChildren()){

                        DataSnapshot addressSnapshot = snapshot.child("ParkingAddress");

                        for (DataSnapshot snapshot1 : addressSnapshot.getChildren()){

                            if (LoginActivity.isMainAdmin == true){

                                if ("Pending".equals(snapshot1.child("status").getValue(String.class))){

                                    mVehicleLocations.add(snapshot1.child("Address").getValue(String.class));
                                    VendorNames.add(snapshot1.child("VendorName").getValue(String.class));
                                    if ("true".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                        mBlock.add(R.drawable.ic_lock_open_black_24dp);

                                    }
                                    else if ("BlockedByAdmin".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                        mBlock.add(R.drawable.ic_lock_black_24dp);

                                    }
                                    else {

                                        mBlock.add(R.drawable.ic_block_black_24dp);

                                    }
                                    mVehicleImages.add(snapshot.child("VehiclePhoto").getValue(String.class));
                                    mVehicleNames.add(snapshot.getKey());
                                    mPricePerHour.add(snapshot.child("PricePerHour").getValue(String.class));
                                    mPricePerDay.add(snapshot.child("PricePerDay").getValue(String.class));
                                    roomUids.add(snapshot1.getKey());
                                    VehiclesList.vendorUids.add(snapshot1.child("VendorUid").getValue(String.class));
                                    noOfVehicles.add(snapshot1.child("NoOfVehicles").getValue(String.class));
                                    mCity.add(CitiesList.cityName);
                                    vehicleType.add(chidSnap.getKey());
                                    mDelete.add(R.drawable.ic_delete_black_24dp);
                                    orderId.add(null);
                                    txnAmount.add(null);
                                    bankTxnId.add(null);
                                    txnId.add(null);
                                    bankName.add(null);

                                }

                            }
                            else {

                                if (LoginActivity.adminUserName.equals(snapshot1.child("AgentId").getValue(String.class))){

                                    if ("Pending".equals(snapshot1.child("status").getValue(String.class))){

                                        mVehicleLocations.add(snapshot1.child("Address").getValue(String.class));
                                        VendorNames.add(snapshot1.child("VendorName").getValue(String.class));
                                        if ("true".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                            mBlock.add(R.drawable.ic_lock_open_black_24dp);

                                        }
                                        else if ("BlockedByAdmin".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                            mBlock.add(R.drawable.ic_lock_black_24dp);

                                        }
                                        else {

                                            mBlock.add(R.drawable.ic_block_black_24dp);

                                        }
                                        mVehicleImages.add(snapshot.child("VehiclePhoto").getValue(String.class));
                                        mVehicleNames.add(snapshot.getKey());
                                        mPricePerHour.add(snapshot.child("PricePerHour").getValue(String.class));
                                        mPricePerDay.add(snapshot.child("PricePerDay").getValue(String.class));
                                        roomUids.add(snapshot1.getKey());
                                        VehiclesList.vendorUids.add(snapshot1.child("VendorUid").getValue(String.class));
                                        noOfVehicles.add(snapshot1.child("NoOfVehicles").getValue(String.class));
                                        mCity.add(CitiesList.cityName);
                                        vehicleType.add(chidSnap.getKey());
                                        mDelete.add(R.drawable.ic_delete_black_24dp);
                                        orderId.add(null);
                                        txnAmount.add(null);
                                        bankTxnId.add(null);
                                        txnId.add(null);
                                        bankName.add(null);

                                    }


                                }

                            }

                        }

                    }

                }

                pd.dismiss();

                mHotelAdapter = new HotelAdapter(VerifyRooms.this,mVehicleImages,VendorNames,mCity,vehicleType,mVehicleNames,
                        mPricePerHour, mPricePerDay,mVehicleLocations,orderId,txnAmount,bankTxnId,txnId,bankName,mDelete,mBlock,noOfVehicles);
                vehiclesRecyclerView.setAdapter(mHotelAdapter);
                vehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                vehiclesRecyclerView.setMotionEventSplittingEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(VerifyRooms.this, "error loading data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
