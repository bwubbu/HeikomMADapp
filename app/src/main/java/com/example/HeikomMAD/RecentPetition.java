package com.example.HeikomMAD;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentPetition extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://heikommad-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Petitions");
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recent_petition, container, false);
        TextView headerDesc = rootView.findViewById(R.id.headerDesc);
        headerDesc.setText("The Society’s Platform for Change");

        ListView petitionList = rootView.findViewById(R.id.petitionList);
        AutoCompleteTextView searchBar = rootView.findViewById(R.id.SearchBar);

        ArrayList<PetitionListItem> list = new ArrayList<>();



        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("PETITION", "Error getting data", task.getException());
                }
                else {
                    for(DataSnapshot ds : task.getResult().getChildren()) {
                        PetitionListItem petition = ds.getValue(PetitionListItem.class);
                        petition.setPetitionID(ds.getKey());
                        list.add(petition);
                    }



                    PetitionListAdapter adapter = new PetitionListAdapter(getContext(), list);

                    adapter.setFragmentClickListener(item -> {
                        Bundle b = new Bundle();
                        b.putString("PetitionID",item.getPetitionID());

                        PetitionDetail p = new PetitionDetail();
                        p.setArguments(b);

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.replace(R.id.petitionMain, p);
                        ft.commit();
                    });
                    petitionList.setAdapter(adapter);

                    ArrayList<String> titleList = new ArrayList<>();

                    list.stream().forEach((e)-> {
                        titleList.add(e.getTitle());
                    });

                    ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, titleList);

                    searchBar.setAdapter(searchAdapter);

                    searchBar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchBar.showDropDown();
                        }
                    });

                    searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ArrayList<PetitionListItem> filteredItem = new ArrayList<>();
                            String title = titleList.get(titleList.indexOf(searchBar.getText().toString()));
                            Log.d("POSITION", position + "");
                            Log.d("title list", titleList.toString() + "");
                            filteredItem.add(list.stream().filter((e)-> e.getTitle().equals(title)).findFirst().orElse(null));
                            PetitionListAdapter filteredAdapter = new PetitionListAdapter(getContext(), filteredItem);
                            petitionList.setAdapter(filteredAdapter);
                        }
                    });

                    ImageButton refreshButton = rootView.findViewById(R.id.refreshButton);
                    refreshButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            searchBar.setText("");
                            petitionList.setAdapter(adapter);
                        }
                    });



                }
            }
        });



        Button CreatePetition = rootView.findViewById(R.id.CreatePetition);
        CreatePetition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.petitionMain, new CreatePetition());
                ft.commit();
            }
        });

        return rootView;

    }
}