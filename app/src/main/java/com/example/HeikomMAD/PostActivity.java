package com.example.HeikomMAD;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    private List<String> myBookmarks;
    private RecyclerView recyclerView_saves;
    private PostAdapter postAdapter_saves;
    private List<Post> postList_saves;

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

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



        searchBar = findViewById(R.id.search_bar);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        titleTextView = findViewById(R.id.titleTextView);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create and set the adapter
        postAdapter = new PostAdapter(postList);
        recyclerView.setAdapter(postAdapter);


        // Initialize Firebase Realtime Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Posts");


        // Fetch data based on the default button (Most Recent)


        // Set onClick listeners for your buttons
        Button mostRecentButton = findViewById(R.id.mostRecent);
        Button mostLikedButton = findViewById(R.id.mostLiked);
        Button mostCommentedButton = findViewById(R.id.mostCommented);
        Button myBookmarksButton = findViewById(R.id.myBookmarks);



        titleTextView.setText("Click on any of the buttons to begin!");


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

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                // Handle submission if needed
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Check if newText is empty; if not, perform search
//                if (!newText.isEmpty()) {
//                    // Perform a search based on the new text
//                    performSearch(newText);
//                } else {
//                    // If the search query is empty, fetch default posts
//                    fetchPostsFromFirebase(lastOrderBy);
//                }
//                return true;
//            }
//        });






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

//    private void performSearch(String searchText) {
//        DatabaseReference searchReference = FirebaseDatabase.getInstance().getReference("Posts");
//
//        searchReference.orderByChild("titleLowerCase")
//                .startAt(searchText.toLowerCase())
//                .endAt(searchText.toLowerCase() + "\uf8ff")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        List<String> postKeys = new ArrayList<>();
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            String postKey = String.valueOf(dataSnapshot.getKey());
//                            postKeys.add(postKey);
//                        }
//
//                        // After getting post keys, perform search using the list
//                        searchPosts(postKeys);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.e("SearchPosts", "Failed to read value.", error.toException());
//                    }
//                });
//    }
//    private void searchPosts(List<String> postKeys) {
//        DatabaseReference searchReference = FirebaseDatabase.getInstance().getReference("Posts");
//        List<Post> postListSearch = new ArrayList<>();
//
//        for (String postKey : postKeys) {
//            DatabaseReference postReference = searchReference.child(postKey);
//
//            postReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Post post = dataSnapshot.getValue(Post.class);
//                    if (post != null) {
//                        post.setKey(postKey);
//                        postListSearch.add(post);
//                    }
//
//                    // Update the UI only when all searched posts are processed
//                    if (postListSearch.size() == postKeys.size()) {
//                        postList.clear();
//                        postList.addAll(postListSearch);
//                        postAdapter.notifyDataSetChanged();
//
//                        // Set a title or update UI as needed
//                        titleTextView.setText("Search Results");
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.e("SearchPosts", "Failed to read value.", error.toException());
//                }
//            });
//        }
//    }






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