package com.example.HeikomMAD;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformativeSection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformativeSection extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Button buttonHelpline;

    FirebaseUser firebaseUser;

    private TextView headerUser;

    private String username;

    private ImageView headerProfilepic;
    private ImageView IVHelpline;

    FirebasePointManager firebasePointManager = new FirebasePointManager();
    public InformativeSection() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InformativeSection.
     */
    // TODO: Rename and change types and number of parameters
    public static InformativeSection newInstance(String param1, String param2) {
        InformativeSection fragment = new InformativeSection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private AA_ActivitiesAdapter adapter;
    AA_ActivitiesAdapter activitiesAdapter = new AA_ActivitiesAdapter(getContext(), new ArrayList<>());
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    //==//==
//    private Context context;
//    private List<FragmentHistorySimpleViewModel> models = new ArrayList<>();
//
//    public void FragmentHistorySimpleAdapter(final List<FragmentHistorySimpleViewModel> viewModels, Context context) {
//
//        if (viewModels != null) {
//            this.models.addAll(viewModels);
//            this.context = context;
//        }
//    }
    //==//==

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_informative_section, container, false);
        headerProfilepic = view.findViewById(R.id.profilePic);
        headerUser = view.findViewById(R.id.headerUser);
        IVHelpline = view.findViewById(R.id.IVHelpline);
        //=====
        Context context = container.getContext();

        //FragmentHistorySimpleAdapter adapter = new FragmentHistorySimpleAdapter(generateSimpleListAllWorkout(), context);
        //=====
        //----------------
        FirebasePointManager firebasePointManager = new FirebasePointManager();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        activitiesAdapter = new AA_ActivitiesAdapter(getContext(), new ArrayList<>());

        // If the user is logged in, show the user profile
        if (firebaseUser != null) {
            showUserProfile(firebaseUser);

            // Initialize user data if it doesn't exist
            firebasePointManager.initializeUserDataIfNotExists(firebaseUser, getContext());
        }


        //---------------
        buttonHelpline  =view.findViewById(R.id.btnHelpline);
        buttonHelpline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HelplineSection() ,null).commit();

            }


        });
        ImageView IVArticle1 = view.findViewById(R.id.IVArticle1);
        IVArticle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentArticle1 = new Intent(Intent.ACTION_VIEW);
                intentArticle1.setData(Uri.parse("https://www.thestar.com.my"));
                startActivity(intentArticle1);
                String articleUrl = "https://www.thestar.com.my";
                ArticleFetcher articleFetcher = new ArticleFetcher();
                articleFetcher.execute(articleUrl);

            }
        });
        ImageView IVArticle2 = view.findViewById(R.id.IVArticle2);
        IVArticle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentArticle1 = new Intent(Intent.ACTION_VIEW);
                intentArticle1.setData(Uri.parse("https://www.thestar.com.my/lifestyle/travel/2023/06/11/whatever-happened-to-the-dream-of-making-kuala-lumpur-a-great-city"));
                startActivity(intentArticle1);
            }
        });
        ImageView IVArticle3 = view.findViewById(R.id.IVArticle3);
        IVArticle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentArticle1 = new Intent(Intent.ACTION_VIEW);
                intentArticle1.setData(Uri.parse("https://www.nst.com.my"));
                startActivity(intentArticle1);
            }
        });
        ImageView IVArticle4 = view.findViewById(R.id.IVArticle4);
        IVArticle4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentArticle1 = new Intent(Intent.ACTION_VIEW);
                intentArticle1.setData(Uri.parse("https://www.malaymail.com"));
                startActivity(intentArticle1);
            }
        });

        return view;
    }
    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    username = firebaseUser.getDisplayName();

                    headerUser.setText("Welcome, " + username + "!");
                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(IVHelpline.getContext()).load(uri).into(headerProfilepic);

//                    if (isAdded()) {
//                        ImageView imageView = getView().findViewById(R.id.headerProfilepic);
//                        Uri imageUrl = uri;
//
//                        if (imageView != null && imageUrl != null) {
//                            Glide.with(IVHelpline.getContext()).load(uri).into(headerProfilepic);
//                        }
//                    }


                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
