package com.example.HeikomMAD;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class AA_CouponCardAdapter extends RecyclerView.Adapter<AA_CouponCardAdapter.MyViewHolder> {
    Context context;
    ArrayList<CardModel> cardModels;
    private SharedPreferences sharedPreferences;

    private int completedTasks = 0;

    public AA_CouponCardAdapter(Context context, ArrayList<CardModel> cardModels){
        this.context = context;
        this.cardModels = cardModels;
        this.sharedPreferences = context.getSharedPreferences("CouponPrefs", Context.MODE_PRIVATE);
        loadClickedStates();
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

        FirebasePointManager pointManager = new FirebasePointManager();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        int points = currentCard.getPoints();
        holder.textPoint.setText(String.valueOf(points));

        // Update UI based on the clicked state of the card
        updateUIForClickedState(currentCard, holder);

        holder.redeemButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (currentUser != null && !currentCard.isClicked()) {
                String userId = currentUser.getUid();

                pointManager.fetchPointsForUser(userId, new FirebasePointManager.PointFetchListener() {
                    @Override
                    public void onPointFetchSuccess(int userPoints) {
                        if (userPoints >= points) {
                            pointManager.deductPointsFromUser(userId, points, new FirebasePointManager.PointUpdateListener() {
                                @Override
                                public void onPointUpdateSuccess() {
                                    currentCard.setClicked(true);
                                    updateUIForClickedState(currentCard, holder);
                                    Toast.makeText(context, "Thank you! Points have been successfully redeemed", Toast.LENGTH_SHORT).show();

                                    //saving the state of the card
                                    currentCard.setClicked(true);
                                    saveClickedState(currentCard.getCardId(), true); // Save state
                                    updateUIForClickedState(currentCard, holder);

                                    // Create and show the coupon details dialog
                                    CouponDetailsDialog dialog = new CouponDetailsDialog(context, position); // Pass the position
                                    dialog.show();
                                }

                                @Override
                                public void onPointUpdateFailure(String message) {
                                    Toast.makeText(context, "Error: " + message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(context, "Sorry, points are not enough", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPointFetchFailure(String message) {
                        Toast.makeText(context, "Error fetching points: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "Coupon already redeemed or user not logged in", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIForClickedState(CardModel currentCard, MyViewHolder holder) {
        if (currentCard.isClicked()) {
            holder.itemView.setAlpha(0.5f); // Set alpha to 0.5 for clicked items
            holder.redeemButton.setEnabled(false); // Disable button for clicked items
        } else {
            holder.itemView.setAlpha(1.0f); // Set alpha to 1.0 for unclicked items
            holder.redeemButton.setEnabled(true); // Enable button for unclicked items
        }
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

    private void loadClickedStates() {
        for (CardModel card : cardModels) {
            boolean isClicked = sharedPreferences.getBoolean(card.getCardId(), false);
            card.setClicked(isClicked);
        }
    }

    private void saveClickedState(String cardId, boolean isClicked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(cardId, isClicked);
        editor.apply();
    }


}