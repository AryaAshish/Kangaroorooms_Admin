package com.architectica.kangaroorooms.theadminapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText newPassword;
    EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newPassword = (EditText)findViewById(R.id.newPasswordEditText);
        confirmPassword = (EditText)findViewById(R.id.confirmPasswordEditText);

    }

    public void changePasswordButton(View view){

        if (newPassword.getText().length() != 0){

            if (newPassword.getText().toString().equals(confirmPassword.getText().toString())){

                Toast.makeText(this, "password changed", Toast.LENGTH_SHORT).show();

                DatabaseReference changePassword = FirebaseDatabase.getInstance().getReference("Admins/" + LoginActivity.adminUid + "/Password");
                changePassword.setValue(newPassword.getText().toString());

                finish();

            }
            else {

                Toast.makeText(this, "Enter the same password in both the fields", Toast.LENGTH_SHORT).show();

            }

        }
        else {

            Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show();

        }

    }

}
