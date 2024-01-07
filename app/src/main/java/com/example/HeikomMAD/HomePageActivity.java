package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

public class HomePageActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private BottomNavigationView bottomNavigationView;
    private String userName;
    private TextView textViewWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        textViewWelcome=findViewById(R.id.textview_show_welcome);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        showUserProfile(firebaseUser);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bmHome) {
                    Intent homeIntent = new Intent(HomePageActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.bmReward) {
                    Intent rewardIntent = new Intent(HomePageActivity.this, RewardsMainActivity.class);
                    startActivity(rewardIntent);
                    return true;
                } else if (itemId == R.id.bmForum) {
                    Intent forumIntent = new Intent(HomePageActivity.this, PostActivity.class);
                    startActivity(forumIntent);
                    return true;
                } else if (itemId == R.id.bmInfo) {
                    Intent infoIntent = new Intent(HomePageActivity.this, PhoneCallPermission.class);
                    startActivity(infoIntent);
                } else if (itemId == R.id.bmUser) {
                    Intent profileIntent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                    startActivity(profileIntent);
                }
                return false;
            }
        });

        TextView feedbackHomepage = findViewById(R.id.feedbackHomepage);

        feedbackHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, FeedbackActivity.class);
                startActivity(intent);
            }
        });

        TextView petitionHomepage = findViewById(R.id.petitionHomePage);

        petitionHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PetitionMainActivity.class);
                startActivity(intent);
            }
        });

        TextView forumHomepage = findViewById(R.id.forumHomepage);
        forumHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        TextView rewardsHomepage = findViewById(R.id.rewardsHomepage);

        rewardsHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, RewardsMainActivity.class);
                startActivity(intent);
            }
        });

        TextView articleHomepage = findViewById(R.id.articleHomepage);

        articleHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PhoneCallPermission.class);
                startActivity(intent);
            }
        });
    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String userID=firebaseUser.getUid();


        //Extracting User Reference from Database "Registezd Users"
        DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails!=null){
                    userName=firebaseUser.getDisplayName();

                    textViewWelcome.setText("Welcome, "+ userName + "!");

                    //Set user DP
                    Uri uri=firebaseUser.getPhotoUrl();


                }else{
                    Toast.makeText(HomePageActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePageActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
    }
}