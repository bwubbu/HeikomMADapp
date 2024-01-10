package com.example.HeikomMAD;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RewardCoupon extends Fragment {

    ArrayList<CardModel> cardModels = new ArrayList<>();

    int[] couponImages = {R.drawable.coupon1,R.drawable.coupon2,R.drawable.coupon3};

    private String username;
    private ImageView headerProfilepic;
    private TextView headerUser;

    public RewardCoupon() {
        // Required empty public constructor
    }

    public static RewardCoupon newInstance() {
        RewardCoupon fragment = new RewardCoupon();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //RecyclerView recyclerView = findViewById(R.id.couponRV);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reward_coupon, container, false);
        headerUser = view.findViewById(R.id.headerUser);
        headerProfilepic = view.findViewById(R.id.headerProfilepic); // Initialize ImageView
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){

        }
        RecyclerView recyclerView = view.findViewById(R.id.couponRV);

        setCardModels();

        AA_CouponCardAdapter adapter = new AA_CouponCardAdapter(getActivity(), cardModels);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view; // Return the inflated view, not a new inflation
    }

    // Save the clicked states of coupons in SharedPreferences
    private void saveClickedStates() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ClickedStates", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (int i = 0; i < cardModels.size(); i++) {
            CardModel currentCard = cardModels.get(i);
            editor.putBoolean("clicked_" + i, currentCard.isClicked());
        }

        editor.apply();
    }

    // Load the clicked states of coupons from SharedPreferences
    private void loadClickedStates() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("ClickedStates", Context.MODE_PRIVATE);

        for (int i = 0; i < cardModels.size(); i++) {
            boolean isClicked = sharedPreferences.getBoolean("clicked_" + i, false);
            float alphaValue = sharedPreferences.getFloat("alpha_" + i, 1.0f);

            cardModels.get(i).setClicked(isClicked);
            cardModels.get(i).setAlphaValue(alphaValue);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        saveClickedStates(); // Save clicked states when the fragment is paused
    }

    @Override
    public void onResume() {
        super.onResume();
        loadClickedStates(); // Load clicked states when the fragment is resumed
    }


    private void setCardModels(){
        //String[] imageName = getResources().getStringArray(R.array.couponImage);
        String[] cardText = getResources().getStringArray(R.array.couponText);
        String[] cardDesc = getResources().getStringArray(R.array.couponDesc);
        int[] points = {100,200,300};

        for(int i=0; i< cardText.length; i++){
            cardModels.add(new CardModel(couponImages[i], cardText[i], cardDesc[i], points[i]));
        }
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    username = firebaseUser.getDisplayName();
                    headerUser.setText("Welcome, " + username + "!");
                    Uri uri = firebaseUser.getPhotoUrl();
                    if (uri != null && headerProfilepic != null && getContext() != null) {
                        Picasso.with(getContext()).load(uri).into(headerProfilepic);
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
}