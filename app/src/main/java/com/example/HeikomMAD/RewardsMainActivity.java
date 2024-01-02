package com.example.HeikomMAD;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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
            else if (item.getItemId() == R.id.bmChat) {
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
