package com.architectica.kangaroorooms.theadminapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

public class LauncherActivity extends AppCompatActivity {

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        String adminUid = getSharedPreferences("AdminUid",MODE_PRIVATE)
                .getString("Uid",null);

        String adminName = getSharedPreferences("AdminName",MODE_PRIVATE)
                .getString("Name",null);

        boolean mainAdmin = getSharedPreferences("MainAdmin",MODE_PRIVATE)
                .getBoolean("IsMainAdmin",false);

        LoginActivity.isMainAdmin = mainAdmin;
        LoginActivity.adminUserName = adminName;
        LoginActivity.adminUid = adminUid;

        if (adminUid != null && adminName != null){

            new Timer().schedule(new TimerTask() {
                public void run() {
                    LauncherActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply();

                            startActivity(new Intent(LauncherActivity.this, MainActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    });
                }
            }, 3000);

        }
        else {

            new Timer().schedule(new TimerTask() {
                public void run() {
                    LauncherActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                    .edit()
                                    .putBoolean("isFirstRun", false)
                                    .apply();

                            startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    });
                }
            }, 3000);

        }

    }
}
