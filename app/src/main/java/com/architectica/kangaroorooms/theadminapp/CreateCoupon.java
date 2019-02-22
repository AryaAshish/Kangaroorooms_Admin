package com.architectica.kangaroorooms.theadminapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCoupon extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText couponCode,couponDescription,discountPercent,applicableName,maxUsableTimes;
    String vehType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_coupon);

        couponCode = (EditText)findViewById(R.id.newCouponCodeEditText);
        couponDescription = (EditText)findViewById(R.id.couponDescriptionEditText);
        discountPercent = (EditText)findViewById(R.id.discountEditText);
        applicableName = (EditText)findViewById(R.id.applicableNameEditText);
        maxUsableTimes = (EditText)findViewById(R.id.maxTimesEditText);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(this);

        List<String> vehicleType = new ArrayList<String>();
        vehicleType.add("All");
        vehicleType.add("Family Rooms");
        vehicleType.add("Single Rooms");
        vehicleType.add("Boys Rooms");
        vehicleType.add("Girls Rooms");
        vehicleType.add("Boys Hostels");
        vehicleType.add("Girls Hostels");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, vehicleType);

        spinner.setAdapter(dataAdapter);

    }

    public void createCouponButton(View view){

        if (couponCode.getText().length() != 0 && couponDescription.getText().length() != 0 && discountPercent.getText().length() != 0 && applicableName.getText().length() != 0 && maxUsableTimes.getText().length() != 0){

            if (Integer.parseInt(discountPercent.getText().toString()) <= 100){

                final DatabaseReference couponsReference = FirebaseDatabase.getInstance().getReference();

                couponsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChild("Coupons")){

                            DataSnapshot snapshot = dataSnapshot.child("Coupons");

                            if (snapshot.hasChild(couponCode.getText().toString())){

                                Toast.makeText(CreateCoupon.this, "Coupon code already present", Toast.LENGTH_SHORT).show();

                            }
                            else {

                                Map<String,String> coupon = new HashMap<String, String>();
                                coupon.put("Description",couponDescription.getText().toString());
                                coupon.put("Discount",discountPercent.getText().toString());
                                coupon.put("VehicleType",vehType);
                                coupon.put("VehicleName",applicableName.getText().toString());
                                coupon.put("MaxTimes",maxUsableTimes.getText().toString());

                                snapshot.getRef().child(couponCode.getText().toString()).setValue(coupon);

                                finish();

                                Toast.makeText(CreateCoupon.this, "Coupon uploaded", Toast.LENGTH_SHORT).show();

                            }

                        }
                        else {

                            Map<String,String> coupon = new HashMap<String, String>();
                            coupon.put("Description",couponDescription.getText().toString());
                            coupon.put("Discount",discountPercent.getText().toString());
                            coupon.put("VehicleType",vehType);
                            coupon.put("VehicleName",applicableName.getText().toString());
                            coupon.put("MaxTimes",maxUsableTimes.getText().toString());

                            FirebaseDatabase.getInstance().getReference("Coupons").child(couponCode.getText().toString()).setValue(coupon);

                            finish();

                            Toast.makeText(CreateCoupon.this, "Coupon uploaded", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            else {

                Toast.makeText(this, "Invalid discount percentage", Toast.LENGTH_SHORT).show();

            }

        }
        else {

            Toast.makeText(this, "please fill all the fields", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        vehType = parent.getItemAtPosition(position).toString();

        Toast.makeText(parent.getContext(),vehType, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
