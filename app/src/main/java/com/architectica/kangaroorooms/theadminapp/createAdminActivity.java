package com.architectica.kangaroorooms.theadminapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class createAdminActivity extends AppCompatActivity {

    EditText newAdminUsername;
    EditText newAdminPassword;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin);

        newAdminUsername = (EditText)findViewById(R.id.newAdminUsernameEditText);
        newAdminPassword = (EditText)findViewById(R.id.newAdminPasswordEditText);

    }

    public void createAdminButton(View view){

        if (newAdminUsername.getText().length() != 0 && newAdminPassword.getText().length() != 0){

            final DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference("Admins");

            adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot admin : dataSnapshot.getChildren()){

                        if (newAdminUsername.getText().toString().equals(admin.child("Username").getValue(String.class))){

                            isAdmin = true;
                            break;

                        }
                        else {

                            isAdmin = false;

                        }

                    }

                    if (isAdmin){

                        Toast.makeText(createAdminActivity.this, "Admin with this username already exists.please choose a different username", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        Toast.makeText(createAdminActivity.this, "Admin created", Toast.LENGTH_SHORT).show();

                        Map<String,String> newAdminMap = new HashMap<String, String>();
                        newAdminMap.put("Username",newAdminUsername.getText().toString());
                        newAdminMap.put("Password",newAdminPassword.getText().toString());

                        adminReference.push().setValue(newAdminMap);

                        finish();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {

            Toast.makeText(this, "Username/password should not be left empty", Toast.LENGTH_SHORT).show();

        }

    }
}
