package com.example.HeikomMAD;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.ArrayList;

public class PetitionPendingAdapter extends ArrayAdapter<PetitionPendingItem> {
    private FragmentActivity activity;
    // invoke the suitable constructor of the ArrayAdapter class
    public PetitionPendingAdapter(@NonNull FragmentActivity activity, Context context, ArrayList<PetitionPendingItem> arrayList) {
        super(context, 0, arrayList);
        this.activity = activity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.petition_pending_item, parent, false);
        }

        PetitionPendingItem item = getItem(position);
        Button readMore = currentItemView.findViewById(R.id.PetitionPButton);
        TextView title = currentItemView.findViewById(R.id.PetitionPTitle);
        TextView author = currentItemView.findViewById(R.id.PetitionPAuthor);

        title.setText(item.getTitle());
        author.setText(item.getAuthor());

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();

                b.putString("PetitionID",item.getPetitionID());

                PetitionDetail p = new PetitionDetail();
                p.setArguments(b);


                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.petitionMain, p);
                ft.commit();
            }
        });

        return currentItemView;
    }
}