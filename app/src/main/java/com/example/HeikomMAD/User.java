package com.example.HeikomMAD;

public class User {
    private String userId;
    private String username;
    private String imageUrl;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String userId, String username, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
