package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText userNameEditText;
    EditText password;
    static String adminUserName;
    static String adminUid;
    boolean isAdmin;
    static boolean isMainAdmin;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameEditText = (EditText)findViewById(R.id.usernameEditText);
        password = (EditText)findViewById(R.id.passwordEditText);

    }

    public void loginButton(View view){

        pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Logging in...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        if (userNameEditText.getText().length() != 0 && password.getText().length() != 0){

            DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference("Admins");

            adminReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot admin : dataSnapshot.getChildren()){

                        if (userNameEditText.getText().toString().equals(admin.child("Username").getValue(String.class)) && password.getText().toString().equals(admin.child("Password").getValue(String.class))){

                            adminUserName = admin.child("Username").getValue(String.class);
                            adminUid = admin.getKey();

                            if (admin.getKey().equals("MainAdmin")){

                                isMainAdmin = true;
                                isAdmin = true;
                                break;

                            }
                            else {

                                isMainAdmin = false;
                                isAdmin = true;
                                break;

                            }

                        }
                        else {

                            isAdmin = false;

                        }

                    }

                    if (isAdmin){

                        getSharedPreferences("AdminUid",MODE_PRIVATE)
                                .edit()
                                .putString("Uid",adminUid)
                                .apply();

                        getSharedPreferences("AdminName",MODE_PRIVATE)
                                .edit()
                                .putString("Name",adminUserName)
                                .apply();

                        getSharedPreferences("MainAdmin",MODE_PRIVATE)
                                .edit()
                                .putBoolean("IsMainAdmin",isMainAdmin)
                                .apply();

                        pd.dismiss();
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }
                    else {

                        pd.dismiss();
                        Toast.makeText(LoginActivity.this, "Invalid username/password", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else {

            pd.dismiss();
            Toast.makeText(this, "Username/password should not be left empty", Toast.LENGTH_SHORT).show();

        }
    }

}
