package com.architectica.kangaroorooms.theadminapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.app.AlertDialog.THEME_HOLO_DARK;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewHolder>{

    Context context;
    List<String> emails;
    List<String> names;
    List<String> phoneNumbers;
    List<String> userUids;
    static String userName;
    static String userEmail;
    static String phoneNumber;
    static String userUid;
    static boolean isBookings;

    public UserAdapter(Context context,List<String> emails,List<String> names,List<String> phoneNumbers,List<String> userUids) {
        this.context = context;
        this.emails = emails;
        this.names = names;
        this.phoneNumbers = phoneNumbers;
        this.userUids = userUids;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.person_list_item,null);

        viewHolder holder = new viewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {

        final String mCurrentEmail = emails.get(i);
        final String mCurrentPhoneNumber = phoneNumbers.get(i);
        final String mCurrentName = names.get(i);
        final String mCurrentUserUid = userUids.get(i);

        viewHolder.email.setText(mCurrentEmail);
        viewHolder.name.setText(mCurrentName);
        viewHolder.phoneNo.setText(mCurrentPhoneNumber);

        if (MainActivity.content.equals("VendorsList")){

            viewHolder.listings.setText("Listings");

        }
        else if (MainActivity.content.equals("UsersList")){

            viewHolder.listings.setText("User Profile");

        }

        viewHolder.listings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = mCurrentName;
                userEmail = mCurrentEmail;
                userUid = mCurrentUserUid;
                phoneNumber = mCurrentPhoneNumber;

                if (MainActivity.content.equals("VendorsList")){

                    Intent intent = new Intent(context,VendorListings.class);
                    context.startActivity(intent);

                }
                else if (MainActivity.content.equals("UsersList")){

                    Intent intent = new Intent(context,UserListings.class);
                    context.startActivity(intent);

                }

                isBookings = false;

            }
        });

        viewHolder.bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = mCurrentName;
                userEmail = mCurrentEmail;
                userUid = mCurrentUserUid;
                phoneNumber = mCurrentPhoneNumber;

                if (MainActivity.content.equals("VendorsList")){

                    Intent intent = new Intent(context,VendorBookings.class);
                    context.startActivity(intent);

                }
                else if (MainActivity.content.equals("UsersList")){

                    Intent intent = new Intent(context,UserBookings.class);
                    context.startActivity(intent);

                }

                isBookings = true;


            }
        });

        viewHolder.deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                builder1.setMessage("Are you sure you want to delete this user?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (MainActivity.content.equals("VendorsList")){

                                    final DatabaseReference deleteReference = FirebaseDatabase.getInstance().getReference("Vendors/" + mCurrentUserUid);

                                    deleteReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.hasChild("UploadedVehicles")){

                                                DataSnapshot snapshot = dataSnapshot.child("UploadedVehicles");

                                                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                                                    String removeVehicleName = dataSnapshot1.child("VehicleName").getValue(String.class);
                                                    final String removeVehicleAddress = dataSnapshot1.child("ParkingAddress").getValue(String.class);
                                                    String removeVehicleType = dataSnapshot1.child("VehicleType").getValue(String.class);
                                                    String removeVehicleCity = dataSnapshot1.child("City").getValue(String.class);
                                                    final String removeVehicleVendor = dataSnapshot.child("Name").getValue(String.class);

                                                    final DatabaseReference removeFromAll = FirebaseDatabase.getInstance().getReference(removeVehicleCity + "/" + removeVehicleType + "/" + removeVehicleName);

                                                    removeFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            DataSnapshot snapshot1 = dataSnapshot.child("ParkingAddress");

                                                            int noOfVendors = (int)snapshot1.getChildrenCount();

                                                            Log.i("no","" + noOfVendors);

                                                            if (noOfVendors<2){
                                                                removeFromAll.removeValue();
                                                            }
                                                            else {

                                                                for (DataSnapshot snapshot2 : snapshot1.getChildren()){

                                                                    if (removeVehicleAddress.equals(snapshot2.child("Address").getValue(String.class)) && removeVehicleVendor.equals(snapshot2.child("VendorName").getValue(String.class))){

                                                                        snapshot2.getRef().removeValue();

                                                                        //also change no of vehicles available after conforming whether no of vehicles is equal to no of parking addresses or not,and whether no of vehicles available should also be uploaded from the vendors app

                                                                    }

                                                                }

                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }

                                                deleteReference.removeValue();

                                            }
                                            else {

                                                deleteReference.removeValue();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                }
                                else if (MainActivity.content.equals("UsersList")){

                                    DatabaseReference deleteReference = FirebaseDatabase.getInstance().getReference("Users/" + mCurrentUserUid);

                                    deleteReference.removeValue();

                                }

                                userUids.remove(mCurrentUserUid);
                                names.remove(mCurrentName);
                                emails.remove(mCurrentEmail);
                                phoneNumbers.remove(mCurrentPhoneNumber);

                                notifyDataSetChanged();

                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }

        });

    }

    @Override
    public int getItemCount() {
        return phoneNumbers.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {


        TextView emailTextView,email,phoneNoTextView,phoneNo,name,nameTextView;
        ImageView personImage;
        LinearLayout clickedLayout;
        Button listings,bookings,deleteUser;

        public viewHolder(@NonNull View itemView) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.personNameTextView);
            name = itemView.findViewById(R.id.personName);
            phoneNo = itemView.findViewById(R.id.phoneNo);
            phoneNoTextView = itemView.findViewById(R.id.phoneNoTextView);
            email = itemView.findViewById(R.id.emailText);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            personImage = itemView.findViewById(R.id.personView);
            clickedLayout = itemView.findViewById(R.id.itemLayout);
            listings = itemView.findViewById(R.id.listings);
            bookings = itemView.findViewById(R.id.bookings);
            deleteUser = itemView.findViewById(R.id.deleteUser);

        }

    }

}
