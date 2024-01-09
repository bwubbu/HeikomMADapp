package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {
    private Firebase ref;
    private EditText feedback;
    private BottomNavigationView bottomNavigationView;
    RelativeLayout layout;

    RatingBar rt;
    Button button;
    float myRating=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback=findViewById(R.id.editTextFeedback);
        layout=findViewById(R.id.relativity);
        Firebase.setAndroidContext(this);
        ref=new Firebase("https://console.firebase.google.com/project/heikommadapp/database/heikommadapp-default-rtdb/data/~2F");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://heikommadapp-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Feedback");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bmHome) {
                    Intent homeIntent = new Intent(FeedbackActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.bmPetition) {
                    Intent rewardIntent = new Intent(FeedbackActivity.this, PetitionMainActivity.class);
                    startActivity(rewardIntent);
                    return true;
                } else if (itemId == R.id.bmForum) {
                    Intent forumIntent = new Intent(FeedbackActivity.this, PostActivity.class);
                    startActivity(forumIntent);
                    return true;
                } else if (itemId == R.id.bmInfo) {
                    Intent infoIntent = new Intent(FeedbackActivity.this, PhoneCallPermission.class);
                    startActivity(infoIntent);
                } else if (itemId == R.id.bmUser) {
                    Intent profileIntent = new Intent(FeedbackActivity.this, UserProfileActivity.class);
                    startActivity(profileIntent);
                }
                return false;
            }
        });






        button=findViewById(R.id.buttonSubmit);
        rt=findViewById(R.id.ratingBar);
        rt.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                int rating=(int) v ;
                String message=null;

                myRating=rt.getRating();
                switch (rating){
                    case 1:
                        message="Sorry to hear that ! :(";
                        break;
                    case 2:
                        message="We always accept suggestion!";
                        break;
                    case 3:
                        message="Good Enough";
                        break;
                    case 4 :
                        message="Great ! Thank you !";
                        break;
                    case 5 :
                        message="Awesome! You are the best ! ";
                        break;
                }
                Toast.makeText(FeedbackActivity.this,message,Toast.LENGTH_SHORT).show();

            }
        });

        Button backbtn=findViewById(R.id.btnBack);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FeedbackActivity.this,HomePageActivity.class);
                startActivity(intent);

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpWindow();
                feedbackSent();
            }

            private void openPopUpWindow() {
                Intent popUpWindow=new Intent(FeedbackActivity.this, PopUpWindow.class);
                startActivity(popUpWindow);
            }
            public void feedbackSent(){
                String feedbackInput=feedback.getText().toString();
                Firebase refFeedBack=ref.child("Feedback");
                refFeedBack.setValue(feedbackInput);
                mDatabase.push().setValue(feedbackInput);
            }



        });






    }}


