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

import com.architectica.kangaroorooms.theadminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserBookings extends AppCompatActivity {

    DatabaseReference userBookingsReference;
    Toolbar toolbar;
    private TextView mToolText;
    List<String> mVehicleNames;
    List<String> mVehicleLocations;
    List<String> mPricePerHour;
    List<String> mPricePerDay;
    List<String> mVehicleImages;
    List<String> VendorNames;
    List<String> noOfVehicles;
    ArrayList<Integer> mDelete = new ArrayList<Integer>(100);
    ArrayList<Integer> mBlock = new ArrayList<Integer>(100);
    List<String> vehicleType;
    List<String> orderId,txnAmount,bankTxnId,txnId,bankName;
    List<String> mCity;
    ProgressDialog pd;
    HotelAdapter mHotelAdapter;
    RecyclerView userBookingsRecyclerView;

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
        setContentView(R.layout.activity_user_bookings);

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
        noOfVehicles = new ArrayList<String>();

        mToolText = (TextView) findViewById(R.id.user_bookings_text);
        mToolText.setText("User Bookings");
        toolbar = (Toolbar) findViewById(R.id.user_bookings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userBookingsRecyclerView = (RecyclerView)findViewById(R.id.user_bookings_recycler_view);

        userBookingsReference = FirebaseDatabase.getInstance().getReference("Users/" + UserAdapter.userUid + "/Bookings");

        pd = new ProgressDialog(UserBookings.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        userBookingsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mPricePerDay.clear();
                mPricePerHour.clear();
                mVehicleLocations.clear();
                mVehicleNames.clear();
                mVehicleImages.clear();
                mCity.clear();
                vehicleType.clear();
                VendorNames.clear();
                mDelete.clear();
                orderId.clear();
                txnId.clear();
                bankName.clear();
                bankTxnId.clear();
                txnAmount.clear();
                mBlock.clear();
                noOfVehicles.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    mVehicleLocations.add(snapshot.child("ParkingAddress").getValue(String.class));
                    VendorNames.add(UserAdapter.userName);
                    mVehicleImages.add(snapshot.child("VehiclePhoto").getValue(String.class));
                    mVehicleNames.add(snapshot.child("VehicleName").getValue(String.class));
                    mPricePerHour.add(snapshot.child("PricePerHour").getValue(String.class));
                    mPricePerDay.add(snapshot.child("PricePerDay").getValue(String.class));
                    mCity.add(CitiesList.cityName);
                    vehicleType.add(snapshot.child("VehicleType").getValue(String.class));
                    mDelete.add(R.drawable.ic_delete_black_24dp);
                    orderId.add(snapshot.child("OrderId").getValue(String.class));
                    txnAmount.add(snapshot.child("TxnAmount").getValue(String.class));
                    bankTxnId.add(snapshot.child("BankTxnId").getValue(String.class));
                    txnId.add(snapshot.child("TxnId").getValue(String.class));
                    bankName.add(snapshot.child("BankName").getValue(String.class));
                    noOfVehicles.add(snapshot.child("BookedNoOfVehicles").getValue(String.class));
                    mBlock.add(R.drawable.ic_block_black_24dp);

                }

                pd.dismiss();

                mHotelAdapter = new HotelAdapter(UserBookings.this,mVehicleImages,VendorNames,mCity,vehicleType,mVehicleNames,
                        mPricePerHour, mPricePerDay,mVehicleLocations,orderId,txnAmount,bankTxnId,txnId,bankName,mDelete,mBlock,noOfVehicles);
                userBookingsRecyclerView.setAdapter(mHotelAdapter);
                userBookingsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                userBookingsRecyclerView.setMotionEventSplittingEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
