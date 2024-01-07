package com.example.HeikomMAD;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class PetitionDetail extends Fragment {
    private String userID;
    private String name;
    private User currentUser;
    private ImageView profilePic;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View Inflater = inflater.inflate(R.layout.fragment_petition_detail, container, false);

        Bundle b = this.getArguments();
        String ID = b.getString("PetitionID");

        // Connect to firebase auth to get current user id
        name = " ";
        userID = " ";

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            // Set userID and name based on the current user
            userID = firebaseUser.getUid();
            name = firebaseUser.getDisplayName();

            // Create a User object
            currentUser = new User(userID, name, null);  // Set imageUrl to null for now, you can set it if needed
        }

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://heikommadapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Petition").child(ID);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PetitionDetailItem Item = snapshot.getValue(PetitionDetailItem.class);
                String signedUserListString = snapshot.child("signedUser").getValue(String.class);
                Item.setSignedUser(signedUserListString);


                TextView headerDesc = Inflater.findViewById(R.id.headerDesc);
                headerDesc.setText("");
                TextView headerUser = Inflater.findViewById(R.id.headerUser);
                headerUser.setText(Item.getTitle());
                profilePic = Inflater.findViewById(R.id.profilePic);

                showUserProfile(firebaseUser);


                TextView author= Inflater.findViewById(R.id.Author);
                TextView date= Inflater.findViewById(R.id.Date);
                TextView council= Inflater.findViewById(R.id.Council);
                TextView target= Inflater.findViewById(R.id.target);
                TextView desc= Inflater.findViewById(R.id.Desc);
                TextView percentage = Inflater.findViewById(R.id.percentage);
                ProgressBar progressBar = Inflater.findViewById(R.id.progressBar);
                ImageView profile = Inflater.findViewById(R.id.authorProfile);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference imageRef = storage.getReference().child(Item.getImagePath());

                final long ONE_MEGABYTE = 1024 * 1024;
                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Glide.with(getContext()).load(bytes).into(profile);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });

                author.setText(Item.getAuthor());
                date.setText(Item.getDate());
                council.setText(Item.getCouncil());
                target.setText( Item.CurrentSignAmount() + " / " + Item.getTarget() + "");
                double completePercent = (double) Item.CurrentSignAmount()/ (double) Item.getTarget() * 100;
                percentage.setText(String.format("%.2f", completePercent ) + "% completed");
                progressBar.setProgress((int) completePercent);

                desc.setText(Item.getDesc());

                Button signButton = Inflater.findViewById(R.id.signButton);

                if (Item.getSignedUserArrayList().contains(currentUser.getUserId()) || Item.getIsSuccess()) {
                    signButton.setVisibility(View.GONE);
                }
                signButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showSignConfirmation(Item.getSignedUserArrayList(),Item.getTarget(), Inflater, mDatabase, ID);
                    }
                });

                ImageButton backButton = Inflater.findViewById(R.id.backButton);
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.petitionMain, new RecentPetition());
                        ft.commit();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return Inflater;
    }

    void showSignConfirmation(ArrayList<String> signedUserList,int target, View Inflater, DatabaseReference mDatabase, String petitionID){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.sign_confirmation);

        final Button sign = dialog.findViewById(R.id.sign);
        final Button cancel = dialog.findViewById(R.id.cancel);

        sign.setOnClickListener((v) -> {
            signedUserList.add(currentUser.getUserId());
            Button signButton = Inflater.findViewById(R.id.signButton);
            TextView targetView= Inflater.findViewById(R.id.target);
            TextView percentage = Inflater.findViewById(R.id.percentage);
            ProgressBar progressBar = Inflater.findViewById(R.id.progressBar);
            targetView.setText( signedUserList.size() + " / " + target + "");
            double completePercent = (double) signedUserList.size()/ (double) target * 100;
            percentage.setText(String.format("%.2f", completePercent ) + "% completed");
            progressBar.setProgress((int) completePercent);
            signButton.setVisibility(View.GONE);

            mDatabase.child("signedUser").setValue(signedUserList.toString());

            if (signedUserList.size() >= target) {
                mDatabase.child("isSuccess").setValue(true);
            }

            dialog.dismiss();
            Toast.makeText(getContext(), "You have successfully signed !", Toast.LENGTH_SHORT).show();
        });

        cancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser){
        String userID=firebaseUser.getUid();

        DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetailsProfile readUserDetails=snapshot.getValue(ReadWriteUserDetailsProfile.class);
                if (readUserDetails!=null){
                    //Set user DP
                    Uri uri=firebaseUser.getPhotoUrl();

                    Picasso.with(getContext()).load(uri).into(profilePic);


                }else{
                    Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
    }
}