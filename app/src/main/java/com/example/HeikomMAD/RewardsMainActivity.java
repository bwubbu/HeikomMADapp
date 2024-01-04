package com.example.HeikomMAD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class RewardsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_main);


        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_heikom);
        NavController navController = navHostFragment.getNavController();

        // Setup BottomNavigationView with NavController
        setupBottomNavMenu(navController);


    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Set navigation listener for BottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.bmHome) {
                // Navigate to the fragment_reward when Home is clicked
                navController.navigate(R.id.rewardHome);
                return true;
            } else if (item.getItemId() == R.id.bmReward) {
                navController.navigate(R.id.rewardReport);
                return true;
            }
            else if (item.getItemId() == R.id.bmForum) {
                navController.navigate(R.id.rewardRedeem);
                return true;
            }
            else if (item.getItemId() == R.id.bmInfo) {
                navController.navigate(R.id.rewardPage);
                return true;
            }


            return false;
        });
    }
}
