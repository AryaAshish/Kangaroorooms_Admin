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

public class VendorListings extends AppCompatActivity {

    DatabaseReference vendorListingsReference;
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
    List<String> noOfVehicles;
    ProgressDialog pd;
    HotelAdapter mHotelAdapter;
    RecyclerView vendorListingsRecyclerView;

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
        setContentView(R.layout.activity_vendor_listings);

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

        mToolText = (TextView) findViewById(R.id.vendor_listings_text);
        mToolText.setText("Landlord Listings");
        toolbar = (Toolbar) findViewById(R.id.vendor_listings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        vendorListingsRecyclerView = (RecyclerView)findViewById(R.id.vendor_listings_recycler_view);

        vendorListingsReference = FirebaseDatabase.getInstance().getReference("Vendors/" + UserAdapter.userUid);

        pd = new ProgressDialog(VendorListings.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vendorListingsReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mPricePerDay.clear();
                        mPricePerHour.clear();
                        mVehicleLocations.clear();
                        mVehicleNames.clear();
                        mVehicleImages.clear();
                        mCity.clear();
                        vehicleType.clear();
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

                        if (dataSnapshot.hasChild("UploadedVehicles")){

                            DataSnapshot snapshot = dataSnapshot.child("UploadedVehicles");

                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                if (LoginActivity.isMainAdmin == true){

                                    if ("Verified".equals(snapshot1.child("status").getValue(String.class))){

                                        mVehicleLocations.add(snapshot1.child("ParkingAddress").getValue(String.class));
                                        VendorNames.add(UserAdapter.userName);
                                        mVehicleImages.add(snapshot1.child("VehiclePhoto").getValue(String.class));
                                        mVehicleNames.add(snapshot1.child("VehicleName").getValue(String.class));
                                        mPricePerHour.add(snapshot1.child("PricePerHour").getValue(String.class));
                                        mPricePerDay.add(snapshot1.child("PricePerDay").getValue(String.class));
                                        mCity.add(CitiesList.cityName);
                                        noOfVehicles.add(snapshot1.child("NoOfVehicles").getValue(String.class));
                                        vehicleType.add(snapshot1.child("VehicleType").getValue(String.class));
                                        mDelete.add(R.drawable.ic_delete_black_24dp);
                                        if ("true".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                            mBlock.add(R.drawable.ic_lock_open_black_24dp);

                                        }
                                        else if ("BlockedByAdmin".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                            mBlock.add(R.drawable.ic_lock_black_24dp);

                                        }
                                        else {

                                            mBlock.add(R.drawable.ic_block_black_24dp);

                                        }
                                        orderId.add(null);
                                        txnAmount.add(null);
                                        bankTxnId.add(null);
                                        txnId.add(null);
                                        bankName.add(null);

                                    }

                                }
                                else {

                                    if (LoginActivity.adminUserName.equals(snapshot1.child("AgentId").getValue(String.class))){

                                        if ("Verified".equals(snapshot1.child("status").getValue(String.class))){

                                            mVehicleLocations.add(snapshot1.child("ParkingAddress").getValue(String.class));
                                            VendorNames.add(UserAdapter.userName);
                                            mVehicleImages.add(snapshot1.child("VehiclePhoto").getValue(String.class));
                                            mVehicleNames.add(snapshot1.child("VehicleName").getValue(String.class));
                                            mPricePerHour.add(snapshot1.child("PricePerHour").getValue(String.class));
                                            mPricePerDay.add(snapshot1.child("PricePerDay").getValue(String.class));
                                            mCity.add(CitiesList.cityName);
                                            noOfVehicles.add(snapshot1.child("NoOfVehicles").getValue(String.class));
                                            vehicleType.add(snapshot1.child("VehicleType").getValue(String.class));
                                            mDelete.add(R.drawable.ic_delete_black_24dp);
                                            if ("true".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                                mBlock.add(R.drawable.ic_lock_open_black_24dp);

                                            }
                                            else if ("BlockedByAdmin".equals(snapshot1.child("isVehicleBlocked").getValue(String.class))){

                                                mBlock.add(R.drawable.ic_lock_black_24dp);

                                            }
                                            else {

                                                mBlock.add(R.drawable.ic_block_black_24dp);

                                            }
                                            orderId.add(null);
                                            txnAmount.add(null);
                                            bankTxnId.add(null);
                                            txnId.add(null);
                                            bankName.add(null);


                                        }

                                    }

                                }


                            }

                            pd.dismiss();

                            mHotelAdapter = new HotelAdapter(VendorListings.this,mVehicleImages,VendorNames,mCity,vehicleType,mVehicleNames,
                                    mPricePerHour, mPricePerDay,mVehicleLocations,orderId,txnAmount,bankTxnId,txnId,bankName,mDelete,mBlock,noOfVehicles);
                            vendorListingsRecyclerView.setAdapter(mHotelAdapter);
                            vendorListingsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            vendorListingsRecyclerView.setMotionEventSplittingEnabled(false);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
