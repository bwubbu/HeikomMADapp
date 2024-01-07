package com.example.HeikomMAD;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Post {
    public String title;

    private FirebaseUser firebaseUser;
    public String description;
    private long timestamp;
    private String key;
    private boolean liked;
    private int likesCount;

    private int commentCount;
    private String titleLowerCase;
    private String userId;

    // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    public Post() {
    }

    public Post(String title, String description, String userId) {
        this.userId = userId;
        this.liked = false;
        this.title = title;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
        this.likesCount = 0;
        this.commentCount=0;
        this.key = key;
        this.titleLowerCase = title.toLowerCase();
    }
    
    // Callback interface for likes count
    public interface OnLikesCountCallback {
        void onLikesCountReceived(long likesCount);

        void onLikesCountError(DatabaseError error);
    }

    public String getTitleLowerCase() {
        return titleLowerCase;
    }

    public void setTitleLowerCase(String titleLowerCase) {
        this.titleLowerCase = titleLowerCase;
    }

    public String getKey() {
        return key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public int getLikesCount() {
        return likesCount;
    }

    public int getCommentCount() {return commentCount;}

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}