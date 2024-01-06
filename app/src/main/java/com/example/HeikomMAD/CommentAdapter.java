package com.example.HeikomMAD;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {


    private Context mContext;
    private List<Comment> mComment;

    private FirebaseUser firebaseuser;


    public CommentAdapter(Context mContext, List<Comment> mComment) {
        this.mContext = mContext;
        this.mComment = mComment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, viewGroup, false);
        return new CommentAdapter.ViewHolder(view, mContext);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComment.get(i);

        viewHolder.comment.setText(comment.getComment());
        viewHolder.showCommentDetails(firebaseuser);

        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("publisherid", comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

        viewHolder.image_profile_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtra("publisherid", comment.getPublisher());
                mContext.startActivity(intent);
            }
        });

    }

    private void showCommentDetails(FirebaseUser firebaseuser) {
    }

    @Override
    public int getItemCount() {
        return mComment.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile_comment;

        private Context context;
        public TextView usernameComment, comment;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            image_profile_comment = itemView.findViewById(R.id.image_profile_comment);
            usernameComment = itemView.findViewById(R.id.usernameComment);
            comment = itemView.findViewById(R.id.comment);

        }

        private void showCommentDetails(FirebaseUser firebaseUser) {
            String userID = firebaseUser.getUid();


            //Extracting User Reference from Database "Registezd Users"
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
            referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetailsProfile readUserDetails = snapshot.getValue(ReadWriteUserDetailsProfile.class);
                    if (readUserDetails != null) {
                        String username = firebaseUser.getDisplayName();
                        usernameComment.setText(username);

                        //Set user DP
                        Uri uri = firebaseUser.getPhotoUrl();
                        Picasso.with(mContext).load(uri).into(image_profile_comment);


                    } else {
                        Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(mContext, "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
