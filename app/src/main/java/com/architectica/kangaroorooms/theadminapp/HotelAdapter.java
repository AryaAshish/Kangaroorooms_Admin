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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.architectica.kangaroorooms.theadminapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import static android.app.AlertDialog.THEME_HOLO_DARK;

/**
 * Created by admin1 on 10/3/18.
 */

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelItemViewHolder> {


    private List<Integer> mDelete;
    private List<Integer> mBlock;
    // List<String> mRoomIds;
    List<String> mHotelNames;
    List<String> mHotelLocations;
    List<String> mOriginalRates;
    List<String> mReducedCosts;
    List<String> mHotelImages;
    List<String> VendorNames;
    List<String> noOfVehicles;
    List<String> orderId;
    List<String> txnAmount;
    List<String> bankTxnId;
    List<String> txnId;
    List<String> bankName;
    private LayoutInflater mInflater;
    private Context context;
    public int row_index;
    List<String> mCity;
    List<String> vehicleType;
    private int width;
    String NoOfVehicles;
    static String bookingUid;
    static String vehicleName,city,type,parkingAddress,vendorName,mainPic;

    public class HotelItemViewHolder extends RecyclerView.ViewHolder {

        public final ImageView hotelImageView;
        public final ImageView favouriteSymbol;
        public final ImageView blockSymbol;
        public final ImageView verifyRoom;
        public final ImageView editSymbol;
        final HotelAdapter mAdapter;
        public final TextView mHotelName;
        public final TextView mHotelCity;
        public final TextView mOriginalCost;
        public final TextView mReducedCost;
        public final TextView mHotelLocation;
        public final TextView mVendorName;
        public final TextView mNoOfVehiclesWithVendor;
        public final LinearLayout mHotelDetailsLayout;

        public HotelItemViewHolder(View itemView,HotelAdapter adapter) {
            super(itemView);
            hotelImageView = (ImageView) itemView.findViewById(R.id.vehicleImage);
            favouriteSymbol = (ImageButton) itemView.findViewById(R.id.favourite_symbol1);
            mHotelName = (TextView) itemView.findViewById(R.id.vehicleName) ;
            mAdapter = adapter;
            mHotelCity = (TextView) itemView.findViewById(R.id.city) ;
            mOriginalCost = (TextView) itemView.findViewById(R.id.pricePerHour);
            mReducedCost = (TextView) itemView.findViewById(R.id.pricePerDay);
            mHotelLocation = (TextView) itemView.findViewById(R.id.parkingAddress);
            mVendorName = (TextView)itemView.findViewById(R.id.vehicleType);
            mHotelDetailsLayout = (LinearLayout) itemView.findViewById(R.id.hotel_details);
            blockSymbol = (ImageView)itemView.findViewById(R.id.block_symbol1);
            editSymbol = (ImageView)itemView.findViewById(R.id.edit);
            mNoOfVehiclesWithVendor = (TextView)itemView.findViewById(R.id.NoOfVehiclesWithThisVendorTextView);
            verifyRoom = (ImageView)itemView.findViewById(R.id.verifyRoom);

        }
    }

    public HotelAdapter(Context context,List<String> mHotelImages,List<String> VendorNames,List<String> mCity,List<String> vehicleType,List<String> mHotelNames,
                        List<String> mOriginalRates,  List<String> mReducedCosts,List<String> mHotelLocations,List<String> orderId,List<String> txnAmount,List<String> bankTxnId,
                        List<String> txnId,List<String> bankName,List<Integer> mDelete,List<Integer> mBlock,List<String> noOfVehicles) {
        mInflater = LayoutInflater.from(context);
        this.mHotelImages = mHotelImages;
        this.VendorNames = VendorNames;
        this.context = context;
        this.mCity = mCity;
        this.vehicleType = vehicleType;
        this.mHotelNames = mHotelNames;
        this.mOriginalRates = mOriginalRates;
        this.mReducedCosts = mReducedCosts;
        this.mHotelLocations = mHotelLocations;
        this.width = width;
        this.orderId = orderId;
        this.txnAmount = txnAmount;
        this.bankTxnId = bankTxnId;
        this.txnId = txnId;
        this.bankName = bankName;
        this.mDelete = mDelete;
        this.mBlock = mBlock;
        this.noOfVehicles = noOfVehicles;
    }
    @Override
    public HotelAdapter.HotelItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.vehicles_list_item, parent, false);
        return new HotelAdapter.HotelItemViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(final HotelAdapter.HotelItemViewHolder holder, final int position) {

        final String mCurentHotelImage = mHotelImages.get(position);
        final String mCurrentHotelName = mHotelNames.get(position) ;
        final String mCurrentOriginalCost = mOriginalRates.get(position);
        final String mCurrentReducedCost = mReducedCosts.get(position);
        final String mCurrentHotelLocation = mHotelLocations.get(position);
        final String mCurrentCity = mCity.get(position);
        final String mCurrentVehType = vehicleType.get(position);
        final String mCurrentOrderId = orderId.get(position);
        final String mCurrentTxnAmount = txnAmount.get(position);
        final String mCurrentBankTxnId = bankTxnId.get(position);
        final String mCurrentTxnId = txnId.get(position);
        final String mCurrentBankName = bankName.get(position);
        final int mCurrentDelete = mDelete.get(position);
        final String mCurrentVendorName = VendorNames.get(position);
        final int mCurrentBlock = mBlock.get(position);
        final String currentNoOfVehicles = noOfVehicles.get(position);

        Picasso.get().load(mCurentHotelImage).into(holder.hotelImageView);
        holder.mHotelCity.setText(mCurrentCity);
        holder.mHotelName.setText(mCurrentHotelName);
        holder.mOriginalCost.setText(mCurrentOriginalCost);
        holder.mReducedCost.setText(mCurrentReducedCost);
        holder.mHotelLocation.setText(mCurrentHotelLocation);
        holder.mVendorName.setText(mCurrentVehType);
        holder.mNoOfVehiclesWithVendor.setText(currentNoOfVehicles + " Vehicles");

        if (MainActivity.content == "VehiclesList"){

            UserAdapter.userUid = VehiclesList.vendorUids.get(position);

        }

        if (UserAdapter.isBookings){

            holder.favouriteSymbol.setVisibility(View.GONE);

            holder.blockSymbol.setVisibility(View.GONE);

            holder.editSymbol.setVisibility(View.GONE);

        }
        else {

            //current vehicle is not booked
            //give the option to delete the vehicle

            holder.favouriteSymbol.setVisibility(View.VISIBLE);
            holder.favouriteSymbol.setImageResource(mCurrentDelete);

            holder.blockSymbol.setVisibility(View.VISIBLE);
            //holder.blockSymbol.setImageResource(mCurrentBlock);
            holder.blockSymbol.setBackgroundResource(mCurrentBlock);

            holder.editSymbol.setVisibility(View.VISIBLE);

        }

        if (MainActivity.content == "Verify Rooms"){

            holder.verifyRoom.setVisibility(View.VISIBLE);

        }
        else {

            holder.verifyRoom.setVisibility(View.GONE);

        }

        holder.favouriteSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                builder1.setMessage("Are you sure you want to delete this uploaded room?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        DatabaseReference referenceToUpdate = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                        String noOfVehicles = dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class);

                                        if (noOfVehicles == "0"){

                                            referenceToUpdate.removeValue();
                                            //do nothing
                                        }
                                        else if (noOfVehicles == "1"){
                                            referenceToUpdate.removeValue();
                                        }
                                        else {

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (mCurrentHotelLocation.equals(snapshot1.child("Address").getValue(String.class)) && mCurrentVendorName.equals(snapshot1.child("VendorName").getValue(String.class))){

                                                    NoOfVehicles = snapshot1.child("NoOfVehicles").getValue(String.class);

                                                }

                                            }


                                            int update = Integer.parseInt(noOfVehicles) - Integer.parseInt(NoOfVehicles);
                                            String updatedNoOfVehicles = Integer.toString(update);

                                            if (update < 1) {

                                                referenceToUpdate.removeValue();

                                            } else {

                                                referenceToUpdate.child("NoOfVehiclesAvailable").setValue(updatedNoOfVehicles);

                                                referenceToUpdate.child("ParkingAddress").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                                            if (mCurrentHotelLocation.equals(snapshot.child("Address").getValue(String.class)) && mCurrentVendorName.equals(snapshot.child("VendorName").getValue(String.class))) {
                                                                snapshot.getRef().removeValue();
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                DatabaseReference vendorReference = FirebaseDatabase.getInstance().getReference("Vendors/" + UserAdapter.userUid + "/UploadedVehicles");

                                vendorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                            if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                snapshot.getRef().removeValue();

                                            }

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

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
            }});

        holder.blockSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mBlock.get(position) == R.drawable.ic_block_black_24dp) {

                    //block the vehicle

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                    builder1.setMessage("Are you sure you want to block this room?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DatabaseReference blockReference = FirebaseDatabase.getInstance().getReference("Vendors/" + UserAdapter.userUid + "/UploadedVehicles");

                                    blockReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                    snapshot.child("isVehicleBlocked").getRef().setValue("BlockedByAdmin");

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                    deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (mCurrentHotelLocation.equals(snapshot1.child("Address").getValue(String.class)) && mCurrentVendorName.equals(snapshot1.child("VendorName").getValue(String.class))) {

                                                    int noOfVeh = Integer.parseInt(snapshot1.child("NoOfVehicles").getValue(String.class));
                                                    int noOfVehAvailable = Integer.parseInt(dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class));

                                                    int update = noOfVehAvailable - noOfVeh;

                                                    if (update>=0){

                                                        dataSnapshot.child("NoOfVehiclesAvailable").getRef().setValue(Integer.toString(update));

                                                    }

                                                    snapshot1.child("isVehicleBlocked").getRef().setValue("BlockedByAdmin");

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    Toast.makeText(context, "Room Blocked", Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    //cancelled deleting the vehicle

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else if (mBlock.get(position) == R.drawable.ic_lock_open_black_24dp){

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                    builder1.setMessage("This room is blocked by the vendor.Are you sure you want to block this room?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DatabaseReference blockReference = FirebaseDatabase.getInstance().getReference("Vendors/" + UserAdapter.userUid + "/UploadedVehicles");

                                    blockReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                    snapshot.child("isVehicleBlocked").getRef().setValue("BlockedByAdmin");

                                                }

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                    deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (mCurrentHotelLocation.equals(snapshot1.child("Address").getValue(String.class)) && mCurrentVendorName.equals(snapshot1.child("VendorName").getValue(String.class))) {

                                                    snapshot1.child("isVehicleBlocked").getRef().setValue("BlockedByAdmin");

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


                                    Toast.makeText(context, "Room Blocked", Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    //cancelled deleting the vehicle

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
                else if(mBlock.get(position) == R.drawable.ic_lock_black_24dp){


                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                    builder1.setMessage("Are you sure you want to unblock this room?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    DatabaseReference blockReference = FirebaseDatabase.getInstance().getReference("Vendors/" + UserAdapter.userUid + "/UploadedVehicles");

                                    blockReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                                if (mCurrentHotelName.equals(snapshot.child("VehicleName").getValue(String.class)) && mCurrentCity.equals(snapshot.child("City").getValue(String.class)) && mCurrentVehType.equals(snapshot.child("VehicleType").getValue(String.class)) && mCurrentHotelLocation.equals(snapshot.child("ParkingAddress").getValue(String.class))) {

                                                    snapshot.child("isVehicleBlocked").getRef().setValue("false");

                                                }

                                            }


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    DatabaseReference deleteFromAll = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/" + mCurrentVehType + "/" + mCurrentHotelName);

                                    deleteFromAll.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            DataSnapshot snapshot = dataSnapshot.child("ParkingAddress");

                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){

                                                if (mCurrentHotelLocation.equals(snapshot1.child("Address").getValue(String.class)) && mCurrentVendorName.equals(snapshot1.child("VendorName").getValue(String.class))) {

                                                    int noOfVeh = Integer.parseInt(snapshot1.child("NoOfVehicles").getValue(String.class));
                                                    int noOfVehAvailable = Integer.parseInt(dataSnapshot.child("NoOfVehiclesAvailable").getValue(String.class));

                                                    int update = noOfVehAvailable + noOfVeh;

                                                    if (update>=0){

                                                        dataSnapshot.child("NoOfVehiclesAvailable").getRef().setValue(Integer.toString(update));

                                                    }

                                                    snapshot1.child("isVehicleBlocked").getRef().setValue("false");

                                                }

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    Toast.makeText(context, "Room Unblocked", Toast.LENGTH_SHORT).show();

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    //cancelled deleting the vehicle

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }

            }
        });

        holder.editSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vehicleName = mCurrentHotelName;
                type = mCurrentVehType;
                city = mCurrentCity;
                vendorName = mCurrentVendorName;
                parkingAddress = mCurrentHotelLocation;
                mainPic = mCurentHotelImage;

                if (MainActivity.content == "VehiclesList"){

                    UserAdapter.userUid = VehiclesList.vendorUids.get(position);
                }

                Intent intent = new Intent(context,EditActivity.class);
                context.startActivity(intent);

            }
        });

        holder.verifyRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference status = FirebaseDatabase.getInstance().getReference(mCurrentCity + "/room/" + mCurrentHotelName + "/ParkingAddress/" + VerifyRooms.roomUids.get(position) + "/status");
                status.setValue("Verified");

                DatabaseReference statusInVendor = FirebaseDatabase.getInstance().getReference("Vendors/" + VehiclesList.vendorUids.get(position) + "/UploadedVehicles/" + VerifyRooms.roomUids.get(position) + "/status");
                statusInVendor.setValue("Verified");

                Toast.makeText(context, "Room Verified", Toast.LENGTH_SHORT).show();

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (UserAdapter.isBookings){

                    DatabaseReference bookings = FirebaseDatabase.getInstance().getReference("Bookings");

                    bookings.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                                if (MainActivity.content.equals("VendorsList")){

                                    if (snapshot.child("VehicleName").getValue(String.class).equals(mCurrentHotelName) && snapshot.child("VehicleType").getValue(String.class).equals(mCurrentVehType) && snapshot.child("VehicleName").getValue(String.class).equals(mCurrentHotelName) && snapshot.child("VendorUid").getValue(String.class).equals(UserAdapter.userUid)){

                                        bookingUid = snapshot.getKey();

                                    }

                                }
                                else if (MainActivity.content.equals("UsersList")){

                                    if (snapshot.child("VehicleName").getValue(String.class).equals(mCurrentHotelName) && snapshot.child("VehicleType").getValue(String.class).equals(mCurrentVehType) && snapshot.child("VehicleName").getValue(String.class).equals(mCurrentHotelName) && snapshot.child("UserUid").getValue(String.class).equals(UserAdapter.userUid)){

                                        bookingUid = snapshot.getKey();

                                    }

                                }
                                else if (MainActivity.content.equals("TodaysBookings")){

                                    Calendar cal = Calendar.getInstance();

                                    if (snapshot.child("VehicleName").getValue(String.class).equals(mCurrentHotelName) && snapshot.child("VehicleType").getValue(String.class).equals(mCurrentVehType) && snapshot.child("VehicleName").getValue(String.class).equals(mCurrentHotelName) && snapshot.child("BookingDate").getValue(String.class).equals(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) + "/" + Integer.toString(cal.get(Calendar.MONTH) + 1) + "/" + Integer.toString(cal.get(Calendar.YEAR)))){

                                        bookingUid = snapshot.getKey();

                                    }

                                }

                            }

                            Intent intent = new Intent(context,HotelActivity_1.class);
                            context.startActivity(intent);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

        });
    }

    @Override
    public int getItemCount() {
        return mHotelImages.size();
    }

    public interface ItemClickListener {
        void onClick(View view, int position, boolean isLongClick);
    }

}
