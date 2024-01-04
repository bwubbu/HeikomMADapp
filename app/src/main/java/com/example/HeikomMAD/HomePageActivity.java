package com.example.HeikomMAD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        Button buttonProfile = findViewById(R.id.ProfilePage);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        Button buttonPetition = findViewById(R.id.PetitionPage);
        buttonPetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PetitionMainActivity.class);
                startActivity(intent);
            }
        });

        Button buttonForum = findViewById(R.id.ForumPage);
        buttonForum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        Button buttonRewards = findViewById(R.id.RewardsPage);
        buttonRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, RewardsMainActivity.class);
                startActivity(intent);
            }
        });

        Button buttonInfoSect = findViewById(R.id.InfoSectPage);
        buttonInfoSect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PhoneCallPermission.class);
                startActivity(intent);
            }
        });
    }
}