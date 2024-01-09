package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class PhoneCallPermission extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_auth);
        setContentView(R.layout.activity_phone_call_permission);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,new InformativeSection(),null);
        fragmentTransaction.commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bmInfo);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bmHome) {
                    Intent homeIntent = new Intent(PhoneCallPermission.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.bmPetition) {
                    Intent rewardIntent = new Intent(PhoneCallPermission.this, PetitionMainActivity.class);
                    startActivity(rewardIntent);
                    return true;
                } else if (itemId == R.id.bmForum) {
                    Intent forumIntent = new Intent(PhoneCallPermission.this, PostActivity.class);
                    startActivity(forumIntent);
                    return true;
                } else if (itemId == R.id.bmInfo) {
                    Intent infoIntent = new Intent(PhoneCallPermission.this, PhoneCallPermission.class);
                    startActivity(infoIntent);
                } else if (itemId == R.id.bmUser) {
                    Intent profileIntent = new Intent(PhoneCallPermission.this, UserProfileActivity.class);
                    startActivity(profileIntent);
                }
                return false;
            }
        });

    }


    public void onBackPressed() {
        // Check if the current fragment is HelplineFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof HelplineSection) {
            // If the current fragment is HelplineFragment, replace it with InformationFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InformativeSection())
                    .commit();
        } else {
            // If not, proceed with the default back button behavior
            super.onBackPressed();
        }
    }
}