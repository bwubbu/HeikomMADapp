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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    private List<String> myBookmarks;

    private String username;
    private BottomNavigationView bottomNavigationView;

    private TextView headerUser;

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
                } else if (itemId == R.id.bmPetition) {
                    Intent rewardIntent = new Intent(PostActivity.this, PetitionMainActivity.class);
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

        readPosts();


        Button mostRecentButton = findViewById(R.id.mostRecent);
        Button mostLikedButton = findViewById(R.id.mostLiked);
        Button mostCommentedButton = findViewById(R.id.mostCommented);
        Button myBookmarksButton = findViewById(R.id.myBookmarks);


        titleTextView.setText("Click on any button to start sorting your posts!☝️");



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
            titleTextView.setText("Most Liked");
            fetchPostsFromFirebases("commentCount");
        });


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
                    Log.d("PostActivity", "Search Snapshot: " + postSnapshot.toString()); // Debug log
                    try {
                        Post post = postSnapshot.getValue(Post.class);
                        if (post != null) {
                            post.setKey(postSnapshot.getKey());
                            postList.add(post);
                        } else {
                            Log.d("PostActivity", "Search Post is null for snapshot: " + postSnapshot.getKey());
                        }
                    } catch (Exception e) {
                        Log.e("PostActivity", "Error in search converting snapshot to Post", e);
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
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
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


    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (searchBar.getText().toString().equals("")) {
                    postList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Log.d("PostActivity", "Snapshot: " + snapshot.toString()); // Debug log
                        try {
                            Post post = snapshot.getValue(Post.class);
                            if (post != null) {
                                post.setKey(snapshot.getKey());
                                postList.add(post);
                            } else {
                                Log.d("PostActivity", "Post is null for snapshot: " + snapshot.getKey());
                            }
                        } catch (Exception e) {
                            Log.e("PostActivity", "Error converting snapshot to Post", e);
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("PostActivity", "Failed to read posts", error.toException());
            }
        });
    }


    private void showCreatePostDialog() {
        // Launch CreatePostActivity
        Intent intent = new Intent(this, CreatePostActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                    Log.d("FirebaseData", "Fetch Snapshot: " + dataSnapshot.toString());
                    try {
                        Post post = dataSnapshot.getValue(Post.class);
                        if (post != null) {
                            post.setKey(String.valueOf(dataSnapshot.getKey()));
                            postList.add(0, post);
                        } else {
                            Log.d("FirebaseData", "Fetch Post is null for snapshot: " + dataSnapshot.getKey());
                        }
                    } catch (Exception e) {
                        Log.e("FirebaseData", "Error in fetchPostsFromFirebase", e);
                    }
                }
                postAdapter.notifyDataSetChanged();
                updateTitle(orderBy);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("MostRecent", "Failed to read value.", error.toException());
            }
        });
    }

    private void fetchPostsFromFirebases(String orderBy) {
        postList.clear();
        postAdapter.notifyDataSetChanged();

        databaseReference.orderByChild(orderBy).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("FirebaseData", "Fetch Snapshot: " + dataSnapshot.toString());
                    try {
                        Post post = dataSnapshot.getValue(Post.class);
                        if (post != null) {
                            post.setKey(String.valueOf(dataSnapshot.getKey()));
                            postList.add(0, post);
                        } else {
                            Log.d("FirebaseData", "Fetch Post is null for snapshot: " + dataSnapshot.getKey());
                        }
                    } catch (Exception e) {
                        Log.e("FirebaseData", "Error in fetchPostsFromFirebase", e);
                    }
                }
                postAdapter.notifyDataSetChanged();
                //updateTitle(orderBy);
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
                readBookmarks(myBookmarks);
                postAdapter.notifyDataSetChanged();
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
//            postAdapter.notifyDataSetChanged();
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