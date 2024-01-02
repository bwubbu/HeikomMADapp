package com.example.HeikomMAD;

public class Post {
    public String title;
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