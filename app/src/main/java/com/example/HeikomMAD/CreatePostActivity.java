package com.example.HeikomMAD;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class CreatePostActivity extends AppCompatActivity {

    private EditText titleInput;
    private EditText descriptionInput;
    private DatabaseReference databaseReference;
    private Dialog createPostDialog;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post);

        // Initialize Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        titleInput = findViewById(R.id.titleinput);
        descriptionInput = findViewById(R.id.descriptioninput);

        Button submitButton = findViewById(R.id.submit_button);
        Button cancelButton = findViewById(R.id.cancel_button);


        // Create the dialog
        createPostDialog = new Dialog(this);
        createPostDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        createPostDialog.setContentView(R.layout.create_post);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the title and description entered by the user
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss", Locale.getDefault());
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kuala_Lumpur"));
                String currentDateandTime = sdf.format(new Date());

                // Check if title and description are not empty
                if (!title.isEmpty() && !description.isEmpty()) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        userId = currentUser.getUid();
                    }
                    Post post = new Post(title, description,userId);

                    // Push the post to the database
                    DatabaseReference newPostRef = databaseReference.push();
                    newPostRef.setValue(post);
                    String postId = newPostRef.getKey();
                    DatabaseReference timeRef = databaseReference.child(postId);
                    timeRef.child("timeStamp").setValue(currentDateandTime);



                    // Display a success message
                    Toast.makeText(getApplicationContext(), "Post successfully created", Toast.LENGTH_SHORT).show();

                    // Optionally, clear the input fields
                    titleInput.setText("");
                    descriptionInput.setText("");

                    // Dismiss the dialog
                    createPostDialog.dismiss();
                    setResult(RESULT_OK);
                    Intent intent = new Intent(CreatePostActivity.this, PostActivity.class);
                    startActivity(intent);
                    //MostRecent fragment = (MostRecent) getSupportFragmentManager().findFragmentById(R.id.recycler_view);
//                    if (fragment != null) {
//                        fragment.addNewPost(post);
//                    }

                } else {
                    // Display an error message if title or description is empty
                    Toast.makeText(getApplicationContext(), "Title and description cannot be empty", Toast.LENGTH_SHORT).show();
                    titleInput.setError("Enter Title");
                    titleInput.requestFocus();
                    descriptionInput.setError("Enter description");
                    descriptionInput.requestFocus();

                }


            }

        });

        // Handle cancel button click
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set   the result as canceled and finish the activity
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}

