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

public class CitiesList extends AppCompatActivity {

    ListView citiesList;
    ArrayList<String> cities = new ArrayList<>();
    TextView noCitiesText;
    static String cityName;
    ProgressDialog pd;
    DatabaseReference citiesReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        citiesList = (ListView)findViewById(R.id.citiesList);
        noCitiesText = (TextView)findViewById(R.id.noCitiesText);

        citiesReference = FirebaseDatabase.getInstance().getReference();

        pd = new ProgressDialog(CitiesList.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        citiesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cities.clear();
                int noOfCities = 0;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    if ("Users".equals(dataSnapshot1.getKey()) || "Vendors".equals(dataSnapshot1.getKey()) || "Admins".equals(dataSnapshot1.getKey()) || "Bookings".equals(dataSnapshot1.getKey()) || "Coupons".equals(dataSnapshot1.getKey())){

                        //do nothing

                    }
                    else {
                        cities.add(dataSnapshot1.getKey());
                        noOfCities++;
                    }

                }

                pd.dismiss();

                if (noOfCities<1){

                    noCitiesText.setVisibility(View.VISIBLE);
                    citiesList.setVisibility(View.GONE);

                }
                else {

                    noCitiesText.setVisibility(View.GONE);
                    citiesList.setVisibility(View.VISIBLE);

                    citiesList.setAdapter(new ArrayAdapter<String>(CitiesList.this, android.R.layout.simple_list_item_1, cities));

                }

                citiesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        cityName = cities.get(position);

                        if ("VehiclesList".equals(MainActivity.content)){

                            Intent intent = new Intent(CitiesList.this,VehiclesList.class);
                            startActivity(intent);

                        }
                        else if ("UsersList".equals(MainActivity.content)){

                            Intent intent = new Intent(CitiesList.this,UsersList.class);
                            startActivity(intent);

                        }
                        else if ("VendorsList".equals(MainActivity.content)){

                            Intent intent = new Intent(CitiesList.this,VendorsList.class);
                            startActivity(intent);

                        }
                        else if ("Verify Rooms".equals(MainActivity.content)){

                            Intent intent = new Intent(CitiesList.this,VerifyRooms.class);
                            startActivity(intent);

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
