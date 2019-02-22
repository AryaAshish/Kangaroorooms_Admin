package com.architectica.kangaroorooms.theadminapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditActivity extends AppCompatActivity {

    EditText vehicleNameEditText;
    EditText noOfVehiclesEditText;
    EditText pricePerHourEditText;
    EditText pricePerDayEditText;
    EditText parkingAddressEditText;
    EditText vendorName;
    ImageView mainPic;
    RecyclerView extraPicsRecyclerView;
    ImageAdapter imageAdapter;
    DatabaseReference vehicleReference;
    List<String> extraPics;
    String remainedVehicles;
    static String updateAddressUid;
    static String vendorUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        vehicleNameEditText = (EditText)findViewById(R.id.vehicleName);
        noOfVehiclesEditText = (EditText)findViewById(R.id.noOfVehicles);
        pricePerHourEditText = (EditText)findViewById(R.id.pricePerHour);
        pricePerDayEditText = (EditText)findViewById(R.id.pricePerDay);
        vendorName = (EditText)findViewById(R.id.vendorName);
        parkingAddressEditText = (EditText)findViewById(R.id.parkingAddress);
        mainPic = (ImageView)findViewById(R.id.vehicleImage);
        extraPicsRecyclerView = (RecyclerView)findViewById(R.id.images_recycler_view);
        extraPics = new ArrayList<String>();

        vehicleReference = FirebaseDatabase.getInstance().getReference(HotelAdapter.city + "/" + HotelAdapter.type + "/" + HotelAdapter.vehicleName);

        vehicleReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                vehicleNameEditText.setText(dataSnapshot.getKey());
                pricePerHourEditText.setText(dataSnapshot.child("PricePerHour").getValue(String.class));
                pricePerDayEditText.setText(dataSnapshot.child("PricePerDay").getValue(String.class));

                DataSnapshot vendorSnapshot = dataSnapshot.child("ParkingAddress");

                for (DataSnapshot child : vendorSnapshot.getChildren()){

                    if (HotelAdapter.vendorName.equals(child.child("VendorName").getValue(String.class)) && HotelAdapter.parkingAddress.equals(child.child("Address").getValue(String.class))){

                        updateAddressUid = child.getKey();
                        vendorUid = child.child("VendorUid").getValue(String.class);
                        vendorName.setText(child.child("VendorName").getValue(String.class));
                        parkingAddressEditText.setText(child.child("Address").getValue(String.class));
                        noOfVehiclesEditText.setText(child.child("NoOfVehicles").getValue(String.class));
                        remainedVehicles = Integer.toString(Integer.parseInt(dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class)) - Integer.parseInt(child.child("NoOfVehicles").getValue(String.class)));
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        vehicleReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                extraPics.clear();

                Picasso.get().load(dataSnapshot.child("VehiclePhoto").getValue(String.class)).into(mainPic);

                DataSnapshot extraPicsSnap = dataSnapshot.child("ExtraImages");

                for (DataSnapshot snapshot : extraPicsSnap.getChildren()){

                    for (DataSnapshot snapshot1 : snapshot.getChildren()){

                        extraPics.add(snapshot1.getValue(String.class));

                    }

                }

                imageAdapter = new ImageAdapter(EditActivity.this,extraPics);
                extraPicsRecyclerView.setAdapter(imageAdapter);
                extraPicsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                extraPicsRecyclerView.setMotionEventSplittingEnabled(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateDetails(View view){

        if (HotelAdapter.vehicleName.equals(vehicleNameEditText.getText().toString())){

            Log.i("entrance","correct");

            vehicleReference.child("PricePerHour").setValue(pricePerHourEditText.getText().toString());
            vehicleReference.child("PricePerDay").setValue(pricePerDayEditText.getText().toString());

            vehicleReference.child("ParkingAddress").child(updateAddressUid).child("Address").setValue(parkingAddressEditText.getText().toString());
            vehicleReference.child("ParkingAddress").child(updateAddressUid).child("VendorName").setValue(vendorName.getText().toString());
            vehicleReference.child("ParkingAddress").child(updateAddressUid).child("NoOfVehicles").setValue(noOfVehiclesEditText.getText().toString());

            vehicleReference.child("NoOfVehiclesAvailable").setValue(Integer.toString(Integer.parseInt(remainedVehicles) + Integer.parseInt(noOfVehiclesEditText.getText().toString())));

            DatabaseReference vendorUpdate = FirebaseDatabase.getInstance().getReference("Vendors/" + vendorUid + "/UploadedVehicles");

            vendorUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot child : dataSnapshot.getChildren()){

                        if (HotelAdapter.vehicleName.equals(child.child("VehicleName").getValue(String.class)) && child.child("VehicleType").getValue(String.class).equals(HotelAdapter.type)){

                            Log.i("test1",noOfVehiclesEditText.getText().toString());
                            Log.i("test2",parkingAddressEditText.getText().toString());

                            child.child("NoOfVehicles").getRef().setValue(noOfVehiclesEditText.getText().toString());
                            child.child("PricePerHour").getRef().setValue(pricePerHourEditText.getText().toString());
                            child.child("PricePerDay").getRef().setValue(pricePerDayEditText.getText().toString());
                            child.child("ParkingAddress").getRef().setValue(parkingAddressEditText.getText().toString());

                        }

                    }

                    Toast.makeText(EditActivity.this, "Vehicle details updated", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            finish();

        }
        else {

            vehicleReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    final DatabaseReference updateReference = FirebaseDatabase.getInstance().getReference(HotelAdapter.city + "/" + HotelAdapter.type);

                    updateReference.child(vehicleNameEditText.getText().toString()).setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                updateReference.child(vehicleNameEditText.getText().toString()).child("PricePerHour").setValue(pricePerHourEditText.getText().toString());
                                updateReference.child(vehicleNameEditText.getText().toString()).child("PricePerDay").setValue(pricePerDayEditText.getText().toString());

                                updateReference.child(vehicleNameEditText.getText().toString()).child("ParkingAddress").child(updateAddressUid).child("Address").setValue(parkingAddressEditText.getText().toString());
                                updateReference.child(vehicleNameEditText.getText().toString()).child("ParkingAddress").child(updateAddressUid).child("VendorName").setValue(vendorName.getText().toString());
                                updateReference.child(vehicleNameEditText.getText().toString()).child("ParkingAddress").child(updateAddressUid).child("NoOfVehicles").setValue(noOfVehiclesEditText.getText().toString());

                                updateReference.child(vehicleNameEditText.getText().toString()).child("NoOfVehiclesAvailable").setValue(Integer.toString(Integer.parseInt(remainedVehicles) + Integer.parseInt(noOfVehiclesEditText.getText().toString())));

                                DatabaseReference vendorUpdate = FirebaseDatabase.getInstance().getReference("Vendors/" + vendorUid + "/UploadedVehicles");

                                vendorUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot child : dataSnapshot.getChildren()){

                                            if (HotelAdapter.vehicleName.equals(child.child("VehicleName").getValue(String.class)) && HotelAdapter.type.equals(child.child("VehicleType").getValue(String.class))){

                                                child.child("VehicleName").getRef().setValue(vehicleNameEditText.getText().toString());
                                                child.child("NoOfVehicles").getRef().setValue(noOfVehiclesEditText.getText().toString());
                                                child.child("PricePerHour").getRef().setValue(pricePerHourEditText.getText().toString());
                                                child.child("PricePerDay").getRef().setValue(pricePerDayEditText.getText().toString());
                                                child.child("ParkingAddress").getRef().setValue(parkingAddressEditText.getText().toString());

                                            }

                                        }

                                        vehicleReference.removeValue();

                                        HotelAdapter.vehicleName = vehicleNameEditText.getText().toString();

                                        Toast.makeText(EditActivity.this, "Vehicle details updated", Toast.LENGTH_SHORT).show();

                                        finish();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

}
