package com.example.HeikomMAD;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AA_CouponCardAdapter extends RecyclerView.Adapter<AA_CouponCardAdapter.MyViewHolder> {
    Context context;
    ArrayList<CardModel> cardModels;

    private int completedTasks = 0;

    public AA_CouponCardAdapter(Context context, ArrayList<CardModel> cardModels){
        this.context = context;
        this.cardModels = cardModels;
    }


    @NonNull
    @Override
    public AA_CouponCardAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout and show the row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.coupon_card, parent, false);

        return new AA_CouponCardAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AA_CouponCardAdapter.MyViewHolder holder, int position) {
        CardModel currentCard = cardModels.get(position);
        holder.imageView.setImageResource(currentCard.getCouponImage());
        holder.textTitle.setText(currentCard.getCouponText());
        holder.textDesc.setText(currentCard.getCouponDesc());

        int points = currentCard.getPoints();
        holder.textPoint.setText(String.valueOf(points));

        if (currentCard.isClicked()) {
            holder.itemView.setAlpha(0.5f); // Set alpha to 0.5 for clicked items
            holder.redeemButton.setEnabled(false); // Disable button for clicked items
        } else {
            holder.itemView.setAlpha(1.0f); // Set alpha to 1.0 for unclicked items
            holder.redeemButton.setEnabled(true); // Enable button for unclicked items
        }

        holder.redeemButton.setOnClickListener(v -> {
            if (!currentCard.isClicked()) {
                String userId = "userId"; // Replace this with your user ID retrieval logic

                boolean isDeducted = PointManager.deductPoints(context, userId, points);
                if (isDeducted) {
                    currentCard.setClicked(true); // Set the clicked state in the CardModel
                    holder.itemView.setAlpha(0.5f); // Change layout alpha to show it's not clickable
                    holder.redeemButton.setEnabled(false); // Disable the button

                    // Increment completedTasks upon successful redemption
                    completedTasks++;

                    // Update the completion status of the task
                    currentCard.setClicked(true);


                    // Handle redemption logic here
                    Toast.makeText(context, "Thank you! Points have been successfully redeemed", Toast.LENGTH_SHORT).show();
                } else {
                    // Insufficient points, handle this scenario
                    Toast.makeText(context, "Sorry, points are not enough", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Notify the user that the coupon has already been redeemed or handle accordingly
                Toast.makeText(context, "Coupon already redeemed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cardModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing view from recycle view and put the data into the layout
        Button redeemButton;

        ImageView imageView;
        TextView textTitle, textDesc, textPoint;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.IVRewardImage);
            textTitle = itemView.findViewById(R.id.TVTaskView);
            textDesc = itemView.findViewById(R.id.TVRewardDescription);
            textPoint = itemView.findViewById(R.id.couponPoints);
            redeemButton = itemView.findViewById(R.id.BtnRedeem);

        }
    }
}