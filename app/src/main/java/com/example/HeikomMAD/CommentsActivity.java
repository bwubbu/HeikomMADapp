package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;

    private CardView imageCardView;

    EditText addComment;
    ImageView image_profile;
    TextView post;

    String postid;
    String publisherid;

    private DatabaseReference databaseReference;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter (this, commentList);
        recyclerView.setAdapter(commentAdapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        addComment = findViewById(R.id.add_commnent);

        imageCardView = findViewById(R.id.image_card_view);
        image_profile = imageCardView.findViewById(R.id.imageprofiletoolbar);
        post = findViewById(R.id.post);

        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid");


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addComment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "You can't send empty comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    addComment();
                    Toast.makeText(CommentsActivity.this, "You commented", Toast.LENGTH_SHORT).show();

                }
            }
        });

        showCommentDetails(firebaseUser);
        readComments();

    }

    private void addComment() {
        // Check if the user is authenticated
        if (firebaseUser != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);

            // Get a new unique key for the comment
            String commentId = reference.child("Comments").push().getKey();

            DatabaseReference commentsReference = reference.child("Comments").child(commentId);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("comment", addComment.getText().toString());
            hashMap.put("publisher", firebaseUser.getUid());

            commentsReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Notify the adapter about the new comment
                        commentAdapter.notifyItemInserted(commentList.size() - 1);
                        addComment.setText("");
                        commentAdapter.notifyDataSetChanged();
                    } else {
                        // Handle the case where adding the comment to the database failed
                        Toast.makeText(CommentsActivity.this, "Failed to add comment", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Handle the case where the user is not authenticated
            Toast.makeText(CommentsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCommentDetails(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();


        //Extracting User Reference from Database "Registezd Users"
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {

                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.with(getApplicationContext()).load(uri).into(image_profile);


                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readComments(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(postid);

        DatabaseReference commentsReference = FirebaseDatabase.getInstance().getReference("Posts").child(postid).child("Comments");

        commentsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                commentAdapter.notifyItemInserted(commentList.size() - 1);
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
