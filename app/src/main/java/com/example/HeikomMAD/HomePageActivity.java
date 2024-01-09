package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    private FirebaseAuth authProfile;
    private BottomNavigationView bottomNavigationView;
    private String userName;
    private TextView textViewWelcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        /*------------------------Hooks----------------------*/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        /*------------------------Tool Bar---------------------*/
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        /*------------------------Nav Drawer Menu---------------------*/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        MenuItem logoutMenuItem = navigationView.getMenu().findItem(R.id.logout);
        logoutMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                showLogoutConfirmationDialog();
                return true;
            }
        });

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
                } else if (itemId == R.id.bmPetition) {
                    Intent rewardIntent = new Intent(HomePageActivity.this, PetitionMainActivity.class);
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

        AppCompatButton forumHomepage = findViewById(R.id.forumHomePage);
        forumHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        AppCompatButton petitionHomepage = findViewById(R.id.petitionHomePage);

        petitionHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PetitionMainActivity.class);
                startActivity(intent);
            }
        });

        AppCompatButton articleHomepage = findViewById(R.id.articleHomePage);

        articleHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, PhoneCallPermission.class);
                startActivity(intent);
            }
        });

        AppCompatButton rewardsHomepage = findViewById(R.id.rewardsHomePage);

        rewardsHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, RewardsMainActivity.class);
                startActivity(intent);
            }
        });

        AppCompatButton feedbackHomepage = findViewById(R.id.feedbackHomePage);

        feedbackHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomePageActivity.this, FeedbackActivity.class);
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
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialogInterface, i) -> logoutUser())
                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomePageActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // ... (Your existing methods)

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.logout) {
            showLogoutConfirmationDialog();
            return true;
        }
        // Handle other menu items as needed
        return true;
    }
}