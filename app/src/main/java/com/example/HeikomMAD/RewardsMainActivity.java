package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
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
    private BottomNavigationView bottomNavigationView;

    private ImageView headerProfilepic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_main);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        headerUser = findViewById(R.id.headerUser);
        headerProfilepic = findViewById(R.id.headerReward);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bmHome) {
                    Intent homeIntent = new Intent(RewardsMainActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.bmPetition) {
                    Intent rewardIntent = new Intent(RewardsMainActivity.this, PetitionMainActivity.class);
                    startActivity(rewardIntent);
                    return true;
                } else if (itemId == R.id.bmForum) {
                    Intent forumIntent = new Intent(RewardsMainActivity.this, PostActivity.class);
                    startActivity(forumIntent);
                    return true;
                } else if (itemId == R.id.bmInfo) {
                    Intent infoIntent = new Intent(RewardsMainActivity.this, PhoneCallPermission.class);
                    startActivity(infoIntent);
                } else if (itemId == R.id.bmUser) {
                    Intent profileIntent = new Intent(RewardsMainActivity.this, UserProfileActivity.class);
                    startActivity(profileIntent);
                }
                return false;
            }
        });


        // Pull user image

        // Find the NavHostFragment
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_heikom);

        // Get the NavController associated with the NavHostFragment
        NavController navController = navHostFragment.getNavController();

        // Navigate to the rewardReport destination
        navController.navigate(R.id.rewardReport);
//        showDetails(firebaseUser);
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




