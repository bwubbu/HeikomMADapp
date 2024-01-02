/*
package com.example.HeikomMAD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {

    private List<String> couponList;

    public CouponAdapter(List<String> couponList) {
        this.couponList = couponList;
    }

    public static class CouponViewHolder extends RecyclerView.ViewHolder {
        ImageView IVRewardImage;
        TextView TVRewardName;
        TextView TVRewardDescription;
        Button BtnRedeem;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            IVRewardImage = itemView.findViewById(R.id.IVRewardImage);
            TVRewardName = itemView.findViewById(R.id.TVRewardName);
            TVRewardDescription = itemView.findViewById(R.id.TVRewardDescription);
            BtnRedeem = itemView.findViewById(R.id.BtnRedeem);
        }
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_card, parent, false);
        return new CouponViewHolder(view);
    }

    @Override    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        // Get the String at the current position in the list
        String currentCoupon = couponList.get(position);

        // Here you can set the String value to your TextViews or any other view as per your requirement
        holder.TVRewardName.setText(currentCoupon);
        // Similarly, set other views if needed

        // You can set click listeners or other interactions here for the buttons, etc.
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }
}
*/