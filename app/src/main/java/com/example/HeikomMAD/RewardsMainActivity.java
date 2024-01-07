package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class RewardsMainActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;

    private TextView headerUser;

    private String username;

    private ImageView headerProfilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_main);

        headerUser = findViewById(R.id.headerUser);
        headerProfilepic = findViewById(R.id.headerProfilepic);

        // Find the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_heikom);

        // Get the NavController associated with the NavHostFragment
        NavController navController = navHostFragment.getNavController();

        // Navigate to the rewardReport destination
        navController.navigate(R.id.rewardReport);
        /*
        // Assuming you have a logged-in FirebaseUser
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            showUserProfile(firebaseUser);
        } else {
            // Handle case where there is no logged-in user
        }

         */
    }

    /*
    private void showUserProfile(FirebaseUser firebaseUser){
        String userID = firebaseUser.getUid();

        // Extracting User Reference from Database "Registered Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null){
                    username = firebaseUser.getDisplayName();

                    headerUser.setText("Welcome, "+ username + "!");

                    // Set user DP
                    Uri uri = firebaseUser.getPhotoUrl();

                    // Ensure you have this ImageView declared and initialized
                    ImageView headerProfilepic = findViewById(R.id.headerProfilepic); // Replace with your actual ImageView ID
                    Picasso.with(RewardsMainActivity.this).load(uri).into(headerProfilepic);

                } else {
                    Toast.makeText(RewardsMainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RewardsMainActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

     */

}


