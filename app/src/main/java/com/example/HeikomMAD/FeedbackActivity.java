package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FeedbackActivity extends AppCompatActivity {
    private Firebase ref;
    private EditText feedback;
    private BottomNavigationView bottomNavigationView;

    RatingBar rt;
    Button button;
    float myRating=0;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedback=findViewById(R.id.editTextFeedback);
        Firebase.setAndroidContext(this);
        ref=new Firebase("https://console.firebase.google.com/project/heikommad/database/heikommad-default-rtdb/data/~2F");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bmHome) {
                    Intent homeIntent = new Intent(FeedbackActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.bmReward) {
                    Intent rewardIntent = new Intent(FeedbackActivity.this, RewardsMainActivity.class);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPopUpWindow();
            }

            private void openPopUpWindow() {
                Intent popUpWindow=new Intent(FeedbackActivity.this, PopUpWindow.class);
                startActivity(popUpWindow);
            }
        });



    }
    public void feedbackSent(View view){
        String feedbackInput=feedback.getText().toString();
        Firebase refFeedBack=ref.child("Feedback");
        refFeedBack.setValue(feedbackInput);
    }

}