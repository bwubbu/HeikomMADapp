package com.example.HeikomMAD;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    private List<String> myBookmarks;

    private String username;
    private BottomNavigationView bottomNavigationView;

    private TextView headerUser, usernamePosts;

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private ImageView headerProfilepic,profilepicPosts;

    private EditText searchBar;
    private String lastOrderBy = "timestamp";
    private List<Post> postList = new ArrayList<>();
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle intent = getIntent().getExtras();
        if (intent != null) {
            String publisher = intent.getString("publisherid");

            SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();

            editor.putString("profileid", publisher);
            editor.apply();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bmForum);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.bmHome) {
                    Intent homeIntent = new Intent(PostActivity.this, HomePageActivity.class);
                    startActivity(homeIntent);
                    return true;
                } else if (itemId == R.id.bmReward) {
                    Intent rewardIntent = new Intent(PostActivity.this, RewardsMainActivity.class);
                    startActivity(rewardIntent);
                    return true;
                } else if (itemId == R.id.bmForum) {
                    Intent forumIntent = new Intent(PostActivity.this, PostActivity.class);
                    startActivity(forumIntent);
                    return true;
                } else if (itemId == R.id.bmInfo) {
                    Intent infoIntent = new Intent(PostActivity.this, PhoneCallPermission.class);
                    startActivity(infoIntent);
                } else if (itemId == R.id.bmUser) {
                    Intent profileIntent = new Intent(PostActivity.this, UserProfileActivity.class);
                    startActivity(profileIntent);
                }
                return false;
            }
        });

        headerProfilepic = findViewById(R.id.headerProfilepic);
        headerUser = findViewById(R.id.headerUser);
        searchBar = findViewById(R.id.search_bar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        titleTextView = findViewById(R.id.titleTextView);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);
//        fetchPostsFromFirebase("timestamp");
        showUserProfile(firebaseUser);


        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");




        Button mostRecentButton = findViewById(R.id.mostRecent);
        Button mostLikedButton = findViewById(R.id.mostLiked);
        Button mostCommentedButton = findViewById(R.id.mostCommented);
        Button myBookmarksButton = findViewById(R.id.myBookmarks);



        titleTextView.setText("Click on any of the buttons to begin!");
        fetchPostsFromFirebase("timestamp");


        myBookmarksButton.setOnClickListener(v -> {
            mybookmarks();
            fetchPostsFromFirebase("bookmarks");
        });


        mostRecentButton.setOnClickListener(v -> {
            fetchPostsFromFirebase("timestamp");
        });

        mostCommentedButton.setOnClickListener(v -> {
            sortPostsByCommentCount();
            fetchPostsFromFirebase("commentCount");
        });


        mostLikedButton.setOnClickListener(v -> {
            sortPostsByLikesCount();
            fetchPostsFromFirebase("likesCount");

        });

        readPosts();
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()) {
                    fetchPostsFromFirebase(lastOrderBy);
                } else {
                    titleTextView.setText("Search Results");
                    searchPosts(s.toString().toLowerCase());

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        findViewById(R.id.createPostButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show the dialog
                showCreatePostDialog();
            }
        });
    }

    private void searchPosts(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Posts")
                .orderByChild("titleLowerCase")
                .startAt(s.toLowerCase())
                .endAt(s.toLowerCase() + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // Directly retrieve the post details from the current snapshot
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        post.setKey(postSnapshot.getKey());
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("SearchPosts", "Failed to read value.", error.toException());
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
                ReadWriteUserDetailsProfile readUserDetails=snapshot.getValue(ReadWriteUserDetailsProfile.class);
                if (readUserDetails!=null){
                    username =firebaseUser.getDisplayName();

                    headerUser.setText("Welcome, "+ username + "!");

//                    usernamePosts.setText(username + ".");

                    //Set user DP
                    Uri uri=firebaseUser.getPhotoUrl();

                    Picasso.with(PostActivity.this).load(uri).into(headerProfilepic);
//                    Picasso.with(PostActivity.this).load(uri).into(profilepicPosts);


                }else{
                    Toast.makeText(PostActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostActivity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        });
    }



    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (searchBar.getText().toString().equals("")){
                    postList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Post post = snapshot.getValue(Post.class);
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void showCreatePostDialog() {
        // Launch CreatePostActivity
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }

    private void sortPostsByLikesCount() {
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return Integer.compare(post2.getLikesCount(), post1.getLikesCount());
            }
        });

        postAdapter.notifyDataSetChanged();
    }

    private void sortPostsByCommentCount() {
        Collections.sort(postList, new Comparator<Post>() {
            @Override
            public int compare(Post post1, Post post2) {
                return Integer.compare(post2.getCommentCount(), post1.getCommentCount());
            }
        });

        postAdapter.notifyDataSetChanged();
    }


    private void fetchPostsFromFirebase(String orderBy) {
        postList.clear();
        postAdapter.notifyDataSetChanged();

        databaseReference.orderByChild(orderBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("FirebaseData", "Post key from dataSnapshot: " + dataSnapshot.getKey());
                    Post post = dataSnapshot.getValue(Post.class);
                    if (post != null) {
                        post.setKey(String.valueOf(dataSnapshot.getKey()));
                        postList.add(0, post);
                    }
                }

                Log.d("FirebaseData", "Number of posts retrieved: " + postList.size());
                postAdapter.notifyDataSetChanged();

                // Explicitly set the title based on the orderBy value
                updateTitle(orderBy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MostRecent", "Failed to read value.", error.toException());
            }
        });
    }

    private void updateTitle(String orderBy) {
        switch (orderBy) {
            case "timestamp":
                titleTextView.setText("Most Recent");
                break;
            case "likesCount":
                titleTextView.setText("Most Liked");
                break;
            case "commentCount":
                titleTextView.setText("Most Commented");
                break;
            case "bookmarks":
                titleTextView.setText("My Bookmarks");
                break;
            // Add cases for other orderBy values as needed
        }
    }



    private void mybookmarks(){
        myBookmarks = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookmarks").child(firebaseUser.getUid());

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String key = String.valueOf(snapshot.getKey());
                    myBookmarks.add(key);
                }
                Log.d("FirebaseData", "Number of bookmarks: " + myBookmarks.size());
                readBookmarks(myBookmarks);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readBookmarks(List<String> myBookmarks) {
        List<Post> postListBookmarked = new ArrayList<>();

        // Check if there are any bookmarks
        if (myBookmarks.isEmpty()) {
            // If no bookmarks, clear the post list
            postList.clear();
            postAdapter.notifyDataSetChanged();
        } else {
            DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("Bookmarks").child(firebaseUser.getUid());

            bookmarksRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        String postKey = postSnapshot.getKey();
                        Boolean isBookmarked = postSnapshot.getValue(Boolean.class);

                        if (isBookmarked != null && isBookmarked) {
                            DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("Posts").child(postKey);

                            postReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    if (!postListBookmarked.contains(post)) {
                                        postListBookmarked.add(post);
                                    }

                                    // Update the UI only when all bookmarks are processed
                                    if (postListBookmarked.size() == myBookmarks.size()) {
                                        postList.clear();
                                        postList.addAll(postListBookmarked);
                                        postAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e("FirebaseData", "Failed to read post data", error.toException());
                                }
                            });
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseData", "Failed to read bookmarks", error.toException());
                }
            });
        }
    }}