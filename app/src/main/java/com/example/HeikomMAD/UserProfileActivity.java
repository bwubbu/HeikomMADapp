package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome,textViewFullName,textViewEmail,textViewDob,textViewGender,textViewPhoneMobile;
    private ProgressBar progressBar;
    private String fullName,email,doB,gender,mobile;
    private ImageView imageView;
    private FirebaseAuth authProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        getSupportActionBar().setTitle("Home");

        textViewWelcome=findViewById(R.id.textview_show_welcome);
        textViewDob=findViewById(R.id.textview_show_dob);
        textViewEmail=findViewById(R.id.textview_show_email);
        textViewGender=findViewById(R.id.textview_show_gendre);
        textViewFullName=findViewById(R.id.textview_show_full_name);
        textViewPhoneMobile=findViewById(R.id.textview_show_phone);
        ProgressBar progressBar=findViewById(R.id.progressBar);

        //set OnClickListener on ImageView to open uploadProfilePicture
        imageView=findViewById(R.id.imageVIew_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this,UploadProfilePictureActivity.class);
                startActivity(intent);
            }
        });


        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        if (firebaseUser==null){
            Toast.makeText(UserProfileActivity.this,"Something went wrong! User's details are not available at the moment",Toast.LENGTH_LONG).show();
        }
        else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

    }
    //Users coming to UserProfileActivity after successful Registration
    private void checkIfEmailVerified(FirebaseUser firebaseUser){
        if (!firebaseUser.isEmailVerified()){
            showAlerDialog();
        }
    }
    private void showAlerDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now .You can not login without email verification next time  .");
        //Open Email Apps if User clicks/taps Continue Button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //To email app in new window and not without
                startActivity(intent);
            }
        });
    }
    private void showUserProfile(FirebaseUser firebaseUser){
        String userID=firebaseUser.getUid();


        //Extracting User Reference from Database "Registed Users"
        DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetailsProfile readUserDetails=snapshot.getValue(ReadWriteUserDetailsProfile.class);
                if (readUserDetails!=null){
                    fullName=firebaseUser.getDisplayName();
                    email=firebaseUser.getEmail();
                    doB=readUserDetails.doB;
                    gender=readUserDetails.gender;
                    mobile = readUserDetails.mobile;

                    textViewWelcome.setText("Welcome, "+ fullName + " !");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDob.setText(doB);
                    textViewGender.setText(gender);
                    textViewPhoneMobile.setText(mobile);

                    //Set user DP
                    Uri uri=firebaseUser.getPhotoUrl();

                    //ImageView setImageURL() should not be used with regular URIs.So we use Picasso
                    Picasso.with(UserProfileActivity.this).load(uri).into(imageView);


                }else{
                    Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }


    //Creating ActionBar Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    //When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.menu_refresh){
            //refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        } else if (id==R.id.menu_update_profile) {
            Intent intent = new Intent(UserProfileActivity.this,UpdateProfileActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }
}