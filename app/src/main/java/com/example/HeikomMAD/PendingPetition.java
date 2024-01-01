package com.example.HeikomMAD;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

public class PendingPetition extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending_petition, container, false);
        TextView headerDesc = rootView.findViewById(R.id.headerDesc);
        headerDesc.setText("The Society’s Platform for Change");

        ListView petitionList = rootView.findViewById(R.id.petitionPList);

        ArrayList<PetitionPendingItem> list = new ArrayList<>(Arrays.asList(new PetitionPendingItem("Pet 1",  "pet", "1"), new PetitionPendingItem("Pet 2",  "pet", "2"), new PetitionPendingItem("Pet 3",  "pet", "3")));

        ArrayAdapter adapter = new PetitionPendingAdapter(getActivity(),getContext(), list);

        petitionList.setAdapter(adapter);

        Button PendingButton = rootView.findViewById(R.id.PendingButton);
        Button ApprovedButton = rootView.findViewById(R.id.ApprovedButton);
        Button RejectedButton = rootView.findViewById(R.id.RejectedButton);

        PendingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ApprovedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RejectedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return rootView;
    }
}