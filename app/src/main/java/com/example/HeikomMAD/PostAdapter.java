package com.example.HeikomMAD;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

// PostAdapter.java
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private FirebaseUser firebaseUser;


    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = postList.get(position);
        holder.getComments(post.getKey(), holder.commentCounts);

        if (post != null) {
            Log.d("PostAdapter", "Post key: " + post.getKey());
            // ... (rest of your code)
        } else {
            Log.e("PostAdapter", "Post is null at position: " + position);
        }

        holder.commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CommentsActivity.class);
                intent.putExtra("postid", post.getKey());
                intent.putExtra("publisherid", post.getUserId());
                view.getContext().startActivity(intent);
            }
        });

        String postKey = post.getKey();
        if (postKey != null) {
            holder.isBookmarked(postKey, holder.bookmark);
        }


//            holder.likes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = holder.getAbsoluteAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        Post post = postList.get(position);
//                        holder.handleLikeClick(post, holder.likes, position);
//                    }
//                }
//            });



        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    String postKey = post.getKey();
                    if (postKey != null) {
                        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference().child("Bookmarks").child(firebaseUser.getUid()).child(postKey);

                        bookmarksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // Post is already bookmarked, remove it
                                    bookmarksRef.removeValue();
                                    holder.bookmark.setImageResource(R.drawable.bookmark_selector);
                                    holder.bookmark.setTag("Bookmark");
                                } else {
                                    // Post is not bookmarked, add it
                                    bookmarksRef.setValue(true);
                                    holder.bookmark.setImageResource(R.drawable.bookmarkpressed);
                                    holder.bookmark.setTag("Bookmarked");
                                }

                                notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle the error if needed
                            }
                        });
                    } else {
                        // Handle the case where postKey is null
                        Log.e("FirebaseData", "Post key is null in onClick");
                    }
                } else {
                    // Handle the case where firebaseUser is null
                    Log.e("FirebaseData", "FirebaseUser is null in onClick");
                }
            }
        });


        holder.isLikes(post.getKey(), holder.likes);
        holder.nrLikes(holder.likesCount, post.getKey());

        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.likes.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getKey())
                            .child(firebaseUser.getUid()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getKey())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.commentCounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CommentsActivity.class);
                intent.putExtra("postid", post.getKey());
                intent.putExtra("publisherid", post.getUserId());
                view.getContext().startActivity(intent);
            }
        });

        // Bind data to the ViewHolder
        holder.bind(post);

//        if (post.isLiked()) {
//            holder.likes.setImageResource(R.drawable.likedicon);
//        } else {
//            holder.likes.setImageResource(R.drawable.likeicon);
//        }
//
//        holder.likesCount.setText(String.valueOf(post.getLikesCount()));
//
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView postTitle, postDescription, likesCount, commentCounts;
        private ImageView likes, imageProfile, commentIcon, bookmark ;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            bookmark = itemView.findViewById(R.id.bookmark);
            commentCounts = itemView.findViewById(R.id.commentsCount);
            commentIcon = itemView.findViewById(R.id.commentIcon);
            // imageProfile = itemView.findViewById(R.id.image_profile);
            likes = itemView.findViewById(R.id.likes);
            likesCount = itemView.findViewById(R.id.likesCount);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);

//            likes.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAbsoluteAdapterPosition();
//                    if (position != RecyclerView.NO_POSITION) {
//                        Post post = postList.get(position);
//                        handleLikeClick(post, likes, position);
//                    }
//                }
//            });
//        }

//        public void handleLikeClick(Post post, ImageView likesImageView, int position) {
//            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//            if (firebaseUser != null) {
//                String postKey = post.getKey();
//                if (postKey != null) {
//                    DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("Likes");
//
//                    likesRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            if (dataSnapshot.exists()) {
//                                // User has already liked, remove the like
//                                likesRef.child(firebaseUser.getUid()).removeValue();
//                                post.setLiked(false);
//
//                            } else {
//                                // User has not liked, add the like
//                                likesRef.child(firebaseUser.getUid()).setValue(true);
//                                post.setLiked(true);
//                            }
//
//                            // Update the likes count in the database
//                            updateLikesCount(postKey, post);
//
//                            // Update the UI immediately after the like is clicked
//                            updateLikeUI(position, post, likesImageView);
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            // Handle the error if needed
//                        }
//                    });
//                } else {
//                    // Handle the case where postKey is null
//                    Log.e("FirebaseData", "Post key is null in handleLikeClick");
//                }
//            } else {
//                // Handle the case where firebaseUser is null
//                Log.e("FirebaseData", "FirebaseUser is null in handleLikeClick");
//            }
//        }
//
//        private void updateLikeUI(int position, Post post, ImageView likesImageView) {
//            if (post.isLiked()) {
//                likesImageView.setImageResource(R.drawable.likedicon);
//            } else {
//                likesImageView.setImageResource(R.drawable.likeicon);
//            }
//
//            likesImageView.setTag(post.isLiked() ? "Liked" : "NotLiked");
//
//            // Notify only the specific item that needs to be updated
//            postList.get(position).setLikesCount(post.getLikesCount());
//            notifyItemChanged(position);
//        }
//
//
//        private void updateLikesCount(String postKey, Post post) {
//            DatabaseReference likesCountRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postKey).child("likesCount");
//
//            likesCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    long currentLikesCount = dataSnapshot.getValue(Long.class);
//
//                    if (post.isLiked()) {
//                        // User liked the post, increase the likes count
//                        currentLikesCount++;
//                    } else {
//                        // User unliked the post, decrease the likes count
//                        currentLikesCount = Math.max(0, currentLikesCount - 1);
//                    }
//
//                    // Update the likes count in the database
//                    likesCountRef.setValue(currentLikesCount);
//                    post.setLikesCount((int) currentLikesCount);
//
//                    // Update the UI
//                    runOnUiThread(() -> notifyDataSetChanged());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle the error if needed
//                }
//            });
        }


        private void isLikes (String postid, ImageView imageView) {
            if (postid != null) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                            imageView.setImageResource(R.drawable.likedicon);
                            imageView.setTag("liked");
                        } else {
                            imageView.setImageResource(R.drawable.likeicon);
                            imageView.setTag("like");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            else {
                Log.e("FirebaseData", "postid is null in isLikes");
            }
        }


        private void nrLikes(TextView likes, String postid) {
            if (postid != null && !postid.isEmpty()) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postid);

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        likes.setText(dataSnapshot.getChildrenCount() + " likes");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
            } else {
                Log.e("FirebaseData", "postid is null or empty in nrLikes");
            }
        }
        private void getComments(String postKey, TextView commentCounts) {
            if (postKey != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postKey).child("Comments");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long commentsCount = snapshot.getChildrenCount();
                        String commentsText = commentsCount + " Comment" + (commentsCount != 1 ? "s" : ""); // Adjust text based on count
                        commentCounts.setText(commentsText);
                        Log.e("FirebaseData", "How many comments:" + commentCounts);

                        updateCommentCountInDatabase(postKey, commentsCount);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseData", "Failed to read comments", error.toException());
                    }
                });
            } else {
                // Handle the case where postKey is null
                Log.e("FirebaseData", "Post key is null in getComments");
            }


        }

        private void runOnUiThread(Runnable action) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(action);
        }

        private void updateCommentCountInDatabase(String postKey, long commentsCount) {
            DatabaseReference postReference = FirebaseDatabase.getInstance().getReference("Posts").child(postKey);
            postReference.child("commentCount").setValue(commentsCount);
        }

        public void bind(Post post) {
            postTitle.setText(post.getTitle());
            postDescription.setText(post.getDescription());
        }

        private void isBookmarked(String postid, ImageView imageView) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser != null) {
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference()
                        .child("Bookmarks")
                        .child(firebaseUser.getUid())
                        .child(postid);

                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isBookmarked = dataSnapshot.exists();

                        if (isBookmarked) {
                            imageView.setImageResource(R.drawable.bookmarkpressed);
                            imageView.setTag("Bookmarked");
                        } else {
                            imageView.setImageResource(R.drawable.bookmark_selector);
                            imageView.setTag("Bookmark");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle the error if needed
                    }
                });
            } else {
                // Handle the case where firebaseUser is null
                Log.e("FirebaseData", "FirebaseUser is null in isBookmarked");
            }
        }
    }
}