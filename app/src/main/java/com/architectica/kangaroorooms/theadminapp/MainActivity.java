package com.architectica.kangaroorooms.theadminapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.architectica.kangaroorooms.theadminapp.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    static String content;
    ViewFlipper viewFlipper;
    GridView gridView;
    int images[] = {R.drawable.logo1,R.drawable.a,R.drawable.b,R.drawable.c};
    Typeface custom_font;
    Calendar c;
    int timeOfDay;
    private Toolbar toolbar;
    public static TextView mwelcomeMessageTxtView;
    TextView changePasswordTextView;
    String welcomeMessage;

    String[] iconNames = {"Rooms","Users","Landlords","Verify Landlords","Verify Rooms","Create Coupon"};
    int[] iconImages = {R.drawable.ic_location_city_black_24dp,R.drawable.profile,R.drawable.profile,R.drawable.profile,R.drawable.ic_add_location_black_24dp,R.drawable.bookingicon};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(toolbar);

        toolbar.inflateMenu(R.menu.menu_items);

        c = Calendar.getInstance();
        timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        mwelcomeMessageTxtView = (TextView)findViewById(R.id.welcome_msg) ;
        changePasswordTextView = (TextView)findViewById(R.id.changePasswordTextView);

        //custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/anagram.ttf");

        if(timeOfDay >= 0 && timeOfDay < 12){
            welcomeMessage = "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            welcomeMessage = "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            welcomeMessage = "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            welcomeMessage = "Good Night";
        }

        mwelcomeMessageTxtView.setText(welcomeMessage+", "+ LoginActivity.adminUserName);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);

        for (int image : images){

            flipImages(image);

        }

        gridView = (GridView) findViewById(R.id.gridview);

        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getApplicationContext(),fruitNames[i],Toast.LENGTH_LONG).show();
               /* Intent intent = new Intent(getApplicationContext(),GridItemActivity.class);
                intent.putExtra("name",fruitNames[i]);
                intent.putExtra("image",fruitImages[i]);
                startActivity(intent);*/

                if (iconNames[i] == "Rooms"){

                    vehiclesList();
                }
                else if (iconNames[i] == "Users"){

                    usersList();
                }
                else if (iconNames[i] == "Landlords"){

                    vendorsList();
                }
                else if (iconNames[i] == "Verify Landlords") {

                    verifyVendors();
                }
                else if (iconNames[i] == "Verify Rooms"){

                    todaysBookings();

                }
                else if (iconNames[i] == "Create Coupon"){

                   createCoupon();

                }

            }
        });

        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changePassword();

            }
        });

    }

    public void flipImages(int image){

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(this,android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(this,android.R.anim.slide_out_right);

    }


    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return iconImages.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.grid_icons,null);
            //getting view in row_data
            TextView name = (TextView) view1.findViewById(R.id.iconName);
            ImageView image = (ImageView) view1.findViewById(R.id.iconImage);

            name.setText(iconNames[i]);

            try {
                image.setImageResource(iconImages[i]);
            }
            catch (Exception e){

                Toast.makeText(MainActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();

            }

            return view1;

        }
    }


    public void vehiclesList(){

        content = "VehiclesList";
        Intent intent = new Intent(MainActivity.this,CitiesList.class);
        startActivity(intent);

    }

    public void usersList(){

        if (LoginActivity.isMainAdmin == true){

            content = "UsersList";
            Intent intent = new Intent(MainActivity.this,UsersList.class);
            startActivity(intent);

        }
        else {

            Toast.makeText(this, "Only the main admin can create new Admins", Toast.LENGTH_SHORT).show();

        }

    }

    public void vendorsList(){

        content = "VendorsList";
        Intent intent = new Intent(MainActivity.this,CitiesList.class);
        startActivity(intent);

    }

    public void verifyVendors(){

        Intent intent = new Intent(MainActivity.this,PendingVendorsList.class);
        startActivity(intent);

    }

    public void todaysBookings(){

        content = "Verify Rooms";
        Intent intent = new Intent(MainActivity.this,CitiesList.class);
        startActivity(intent);

    }

    public void createCoupon(){

        Intent intent = new Intent(MainActivity.this,CreateCoupon.class);
        startActivity(intent);

    }

    public void changePassword(){

        Intent intent = new Intent(MainActivity.this,ChangePasswordActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_items,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.createAdmin){

            if (LoginActivity.isMainAdmin == true){

                Intent intent = new Intent(MainActivity.this,createAdminActivity.class);
                startActivity(intent);

            }
            else {

                Toast.makeText(this, "Only the main admin can create new Admins", Toast.LENGTH_SHORT).show();

            }

        }
        else if (item.getItemId() == R.id.logOut){

            //logout the user
            //remove the static values

            getSharedPreferences("AdminUid",MODE_PRIVATE)
                    .edit()
                    .putString("Uid",null)
                    .apply();

            getSharedPreferences("AdminName",MODE_PRIVATE)
                    .edit()
                    .putString("Name",null)
                    .apply();

            getSharedPreferences("MainAdmin",MODE_PRIVATE)
                    .edit()
                    .putBoolean("IsMainAdmin",false)
                    .apply();

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        else if (item.getItemId() == R.id.deleteCoupon){

            Intent intent = new Intent(MainActivity.this,CouponsActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

}
