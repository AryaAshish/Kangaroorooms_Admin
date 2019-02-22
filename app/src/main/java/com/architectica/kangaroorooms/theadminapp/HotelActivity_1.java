package com.architectica.kangaroorooms.theadminapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HotelActivity_1 extends AppCompatActivity {

    TextView VendorName, VehicleName, ParkingAddress, NoOfVehiclesBooked, VehicleCity, OrderId, TxnAmount, BankTxnId, TxnId, BankName, BookingHours, BookingUserName, PickUp, Delivery;
    ImageView VehicleImage;
    TextView balanceAmount, totalAmount,payableAmount;
    LinearLayout bookingDetailsLayout;
    ProgressDialog pd;
    TextView txnDate;
    TextView customerPhoneNumber, customerEmail;
    RelativeLayout discountLayout, couponLayout;
    TextView couponCode, discount;
    LinearLayout detailsLayout;
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_1);

        VendorName = (TextView) findViewById(R.id.vendorName);
        VehicleName = (TextView) findViewById(R.id.vehiclesName);
        ParkingAddress = (TextView) findViewById(R.id.vendorsParkingAddress);
        NoOfVehiclesBooked = (TextView) findViewById(R.id.NoOfVehiclesWithThisVendor);
        VehicleCity = (TextView) findViewById(R.id.vehicleCity);
        OrderId = (TextView) findViewById(R.id.orderId);
        TxnAmount = (TextView) findViewById(R.id.transactionAmount);
        BankTxnId = (TextView) findViewById(R.id.bankTxnId);
        TxnId = (TextView) findViewById(R.id.transactionId);
        BankName = (TextView) findViewById(R.id.bankName);
        BookingHours = (TextView) findViewById(R.id.bookingHours);
        BookingUserName = (TextView) findViewById(R.id.userName);
        PickUp = (TextView) findViewById(R.id.PickUp);
        Delivery = (TextView) findViewById(R.id.Delivery);
        VehicleImage = (ImageView) findViewById(R.id.vehiclesImage);
        bookingDetailsLayout = (LinearLayout) findViewById(R.id.bookingDetailsLayout);
        balanceAmount = (TextView) findViewById(R.id.balanceAmount);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        txnDate = (TextView) findViewById(R.id.txnDateTextView);
        customerPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
        customerEmail = (TextView) findViewById(R.id.email);
        discountLayout = (RelativeLayout) findViewById(R.id.discountLayout);
        couponLayout = (RelativeLayout) findViewById(R.id.couponLayout);
        discount = (TextView) findViewById(R.id.discount);
        couponCode = (TextView) findViewById(R.id.couponCode);
        payableAmount = (TextView)findViewById(R.id.payableAmount);
        detailsLayout = (LinearLayout)findViewById(R.id.detailsLayout);
        titleText = (TextView)findViewById(R.id.titleText);

        pd = new ProgressDialog(HotelActivity_1.this);
        pd.setMessage("Loading...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference("Bookings/" + HotelAdapter.bookingUid);

        bookingReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                bookingDetailsLayout.setVisibility(View.VISIBLE);

                Picasso.get().load(dataSnapshot.child("VehiclePhoto").getValue(String.class)).into(VehicleImage);
                VendorName.setText(dataSnapshot.child("Vendor").getValue(String.class));
                VehicleName.setText(dataSnapshot.child("VehicleName").getValue(String.class));
                ParkingAddress.setText(dataSnapshot.child("ParkingAddress").getValue(String.class));
                NoOfVehiclesBooked.setText(dataSnapshot.child("BookedNoOfVehicles").getValue(String.class));
                VehicleCity.setText(dataSnapshot.child("City").getValue(String.class));
                OrderId.setText(dataSnapshot.child("OrderId").getValue(String.class));
                TxnAmount.setText(dataSnapshot.child("TxnAmount").getValue(String.class));
                BankTxnId.setText(dataSnapshot.child("BankTxnId").getValue(String.class));
                TxnId.setText(dataSnapshot.child("TxnId").getValue(String.class));
                BankName.setText(dataSnapshot.child("BankName").getValue(String.class));
                BookingHours.setText(dataSnapshot.child("BookedInterval").getValue(String.class) + " Hours");
                PickUp.setText(dataSnapshot.child("PickUp").getValue(String.class));
                Delivery.setText(dataSnapshot.child("Delivery").getValue(String.class));
                txnDate.setText(dataSnapshot.child("TxnDate").getValue(String.class));
                totalAmount.setText(dataSnapshot.child("TotalAmount").getValue(String.class));
                balanceAmount.setText(dataSnapshot.child("BalanceAmount").getValue(String.class));
                payableAmount.setText(dataSnapshot.child("BalanceAmount").getValue(String.class));

                if (dataSnapshot.hasChild("CouponCode")) {

                    discountLayout.setVisibility(View.VISIBLE);
                    couponLayout.setVisibility(View.VISIBLE);

                    discount.setText(dataSnapshot.child("Discount").getValue(String.class));
                    couponCode.setText(dataSnapshot.child("CouponCode").getValue(String.class));

                }

                if (MainActivity.content.equals("VendorsList")){

                    //vendor bookings

                    detailsLayout.setVisibility(View.VISIBLE);
                    titleText.setText("Customer Details : ");

                    BookingUserName.setText(dataSnapshot.child("UserName").getValue(String.class));

                    String userUid = dataSnapshot.child("UserUid").getValue(String.class);

                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users/" + userUid);

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            DataSnapshot dataSnapshot1 = dataSnapshot.child("PhoneNumber");

                            for (DataSnapshot snapshot : dataSnapshot1.getChildren()) {

                                customerPhoneNumber.setText(snapshot.getValue(String.class));

                            }

                            DataSnapshot dataSnapshot2 = dataSnapshot.child("Email");

                            for (DataSnapshot snapshot : dataSnapshot2.getChildren()) {

                                customerEmail.setText(snapshot.getValue(String.class));

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
                else if (MainActivity.content.equals("UsersList")){

                    //user bookings

                    detailsLayout.setVisibility(View.VISIBLE);
                    titleText.setText("Vendor Details : ");

                    BookingUserName.setText(dataSnapshot.child("Vendor").getValue(String.class));

                    String userUid = dataSnapshot.child("VendorUid").getValue(String.class);

                    DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Vendors/" + userUid);

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            customerPhoneNumber.setText(dataSnapshot.child("PhoneNumber").getValue(String.class));

                            customerEmail.setText(dataSnapshot.child("Email").getValue(String.class));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                pd.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();
            }
        });

    }

}

