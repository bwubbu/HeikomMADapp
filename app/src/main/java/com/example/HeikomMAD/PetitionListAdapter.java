package com.example.HeikomMAD;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PetitionListAdapter extends ArrayAdapter<PetitionListItem> {

    // invoke the suitable constructor of the ArrayAdapter class
    public PetitionListAdapter(@NonNull Context context, ArrayList<PetitionListItem> arrayList) {
        super(context, 0, arrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.petition_list_item, parent, false);
        }

        PetitionListItem item = getItem(position);
        Button readMore = currentItemView.findViewById(R.id.PetitionButton);
        TextView title = currentItemView.findViewById(R.id.PetitionTitle);
        TextView desc = currentItemView.findViewById(R.id.PetitionDesc);
        TextView author = currentItemView.findViewById(R.id.PetitionAuthor);
        ImageView image = currentItemView.findViewById(R.id.PetitionImage);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(item.getImagePath());

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Glide.with(getContext()).load(bytes).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        title.setText(item.getTitle());
        desc.setText(item.getDesc());
        author.setText(item.getAuthor());

        readMore.setOnClickListener(view -> {
            // Pass the click event to the fragment
            if (fragmentClickListener != null) {
                fragmentClickListener.onButtonClick(item);
            }
        });

        return currentItemView;
    }

    public interface FragmentClickListener {
        void onButtonClick(PetitionListItem item);
    }

    private FragmentClickListener fragmentClickListener;

    // Setter method for the click listener
    public void setFragmentClickListener(FragmentClickListener listener) {
        this.fragmentClickListener = listener;
    }
}