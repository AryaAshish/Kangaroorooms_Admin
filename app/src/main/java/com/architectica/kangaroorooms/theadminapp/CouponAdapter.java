package com.architectica.kangaroorooms.theadminapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
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

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.viewHolder>{

    Context context;
    List<String> couponCodes;
    List<String> couponDescriptions;
    List<String> applicableTypes;
    List<String> applicableNames;
    List<String> maxApplicableTimes;

    public CouponAdapter(Context context,List<String> couponCodes,List<String> couponDescriptions,List<String> applicableTypes,List<String> applicableNames,List<String> maxApplicableTimes){

        this.context = context;
        this.couponCodes = couponCodes;
        this.couponDescriptions = couponDescriptions;
        this.applicableTypes = applicableTypes;
        this.applicableNames = applicableNames;
        this.maxApplicableTimes = maxApplicableTimes;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coupon_card_view,null);

        viewHolder holder = new viewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {

        final String mCurrentCouponCode = couponCodes.get(position);
        final String mCurrentCouponDescription = couponDescriptions.get(position);
        final String mCurrentApplicableType = applicableTypes.get(position);
        final String mCurrentApplicableName = applicableNames.get(position);
        final String mCurrentMaxApplicableTimes = maxApplicableTimes.get(position);

        holder.couponCode.setText(mCurrentCouponCode);
        holder.couponDescription.setText(mCurrentCouponDescription);
        holder.applicableName.setText("applicable on " + mCurrentApplicableName + " vehicle of " + mCurrentApplicableType + " type");

        holder.maxApplicableTime.setText("Maximum number of times a user can avail discount using this coupon is " + mCurrentMaxApplicableTimes);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context, THEME_HOLO_DARK);
                builder1.setMessage("Are you sure you want to delete this coupon?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                DatabaseReference couponReference = FirebaseDatabase.getInstance().getReference("Coupons");

                                couponReference.child(mCurrentCouponCode).removeValue();

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

    }

    @Override
    public int getItemCount() {
        return couponCodes.size();
    }

    class viewHolder extends RecyclerView.ViewHolder {

        TextView couponCode,couponDescription,applicableName,maxApplicableTime;
        ImageView deleteButton;

        public viewHolder(@NonNull View itemView) {

            super(itemView);

            couponCode = (TextView) itemView.findViewById(R.id.couponCode);
            couponDescription = (TextView)itemView.findViewById(R.id.couponDescription);
            applicableName = (TextView)itemView.findViewById(R.id.applicableName);
            maxApplicableTime = (TextView)itemView.findViewById(R.id.maxTimes);
            deleteButton = (ImageView)itemView.findViewById(R.id.deleteSymbol);

        }

    }


}

