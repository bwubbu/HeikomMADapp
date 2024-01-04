package com.example.HeikomMAD;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InformativeSection#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InformativeSection extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button buttonHelpline;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informative_section, container, false);
        // Assuming you have two views, view1 and view2


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

}