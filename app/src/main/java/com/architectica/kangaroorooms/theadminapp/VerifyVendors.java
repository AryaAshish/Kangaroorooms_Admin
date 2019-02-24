package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.architectica.kangaroorooms.theadminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class VerifyVendors extends AppCompatActivity {

    DatabaseReference vendorReference,statusReference;
    TextView name;
    TextView mobileNumber,email,aadharNumber,address;
    ImageView aadharImage;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_vendors);

        name = (TextView)findViewById(R.id.name);
        mobileNumber = (TextView)findViewById(R.id.mobileNumber);
        email = (TextView)findViewById(R.id.email);
        aadharNumber = (TextView)findViewById(R.id.aadharNumber);
        address = (TextView)findViewById(R.id.address);
        aadharImage = (ImageView)findViewById(R.id.aadharImage);

        pd = new ProgressDialog(VerifyVendors.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        vendorReference = FirebaseDatabase.getInstance().getReference("Vendors/" + PendingVendorsList.pendingVendorUid);

        vendorReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.child("AadharUrl").getValue(String.class);

                name.setText(dataSnapshot.child("Name").getValue(String.class));
                mobileNumber.setText(dataSnapshot.child("PhoneNumber").getValue(String.class));
                email.setText(dataSnapshot.child("Email").getValue(String.class));
                aadharNumber.setText(dataSnapshot.child("AadharNumber").getValue(String.class));
                address.setText(dataSnapshot.child("VendorAddress").getValue(String.class));
                Picasso.get().load(url).into(aadharImage, new Callback() {
                    @Override
                    public void onSuccess() {

                        if (Build.VERSION.SDK_INT<17){

                            if (VerifyVendors.this.isFinishing()) { // or call isFinishing() if min sdk version < 17
                                return;
                            }

                            pd.dismiss();

                        }
                        else {

                            if (VerifyVendors.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
                                return;
                            }

                            pd.dismiss();
                        }

                    }
                    @Override
                    public void onError(Exception e) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

            }
        });

    }

    @Override
    protected void onDestroy() {

        pd.dismiss();
        super.onDestroy();
    }

    public void verifyButton(View view){

        statusReference = FirebaseDatabase.getInstance().getReference("Vendors/" + PendingVendorsList.pendingVendorUid + "/Status");
        statusReference.setValue("Verified");

        Toast.makeText(this, "Vendor Verified", Toast.LENGTH_SHORT).show();

        finish();

    }
}
