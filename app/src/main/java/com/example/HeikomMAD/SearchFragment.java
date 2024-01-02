package com.example.HeikomMAD;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchFragment extends Fragment {
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;

    EditText search_bar;

//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.fragment_search, container, false);
//
//            recyclerView = view.findViewById(R.id.recycler_view);
//
//    }
}