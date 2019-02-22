package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.architectica.kangaroorooms.theadminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingVendorsList extends AppCompatActivity {

    ListView pendingVendorsList;
    TextView noPendingVendorsText;
    ArrayList<String> pendingVendors = new ArrayList<String>();
    DatabaseReference pendingVendorsReference;
    static String pendingVendorUid;
    ArrayList<String> pendingVendorsUidList = new ArrayList<>();
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_vendors_list);

        pendingVendorsList = (ListView)findViewById(R.id.pendingVendorsList);
        noPendingVendorsText = (TextView)findViewById(R.id.noPendingVendorsText);

        pendingVendorsReference = FirebaseDatabase.getInstance().getReference("Vendors");

        pd = new ProgressDialog(PendingVendorsList.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        pendingVendorsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int noOfPendingVendors = 0;
                pendingVendorUid = null;
                pendingVendors.clear();
                pendingVendorsUidList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    if (snapshot.hasChild("Status")){

                        if ("Pending".equals(snapshot.child("Status").getValue(String.class))){

                            pendingVendors.add(snapshot.child("Name").getValue(String.class));
                            pendingVendorsUidList.add(snapshot.getKey());
                            noOfPendingVendors++;

                        }

                    }

                }

                pd.dismiss();

                if (noOfPendingVendors<1){

                    noPendingVendorsText.setVisibility(View.VISIBLE);
                    pendingVendorsList.setVisibility(View.GONE);

                }
                else {

                    noPendingVendorsText.setVisibility(View.GONE);
                    pendingVendorsList.setVisibility(View.VISIBLE);

                    pendingVendorsList.setAdapter(new ArrayAdapter<String>(PendingVendorsList.this, android.R.layout.simple_list_item_1, pendingVendors));

                }

                pendingVendorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        pendingVendorUid = pendingVendorsUidList.get(position);
                        Intent intent = new Intent(PendingVendorsList.this,VerifyVendors.class);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

            }
        });

    }
}
