package com.example.HeikomMAD;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class HelplineSection extends Fragment {
    Button btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helpline_section, container, false);
        Button btnPolice = view.findViewById(R.id.btnPolice);
        btnPolice.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             Intent policeIntent = new Intent(Intent.ACTION_DIAL);
                                             policeIntent.setData(Uri.parse("tel:0173948957"));
                                             getActivity().startActivity(policeIntent);
                                         }
                                     }
        );

        Button btnAmbulance = view.findViewById(R.id.btnAmbulance);
        btnAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ambulanceIntent = new Intent(Intent.ACTION_DIAL);
                ambulanceIntent.setData(Uri.parse("tel:0173948957"));
                startActivity(ambulanceIntent);
            }
        });
        Button btnFiremen = view.findViewById(R.id.btnFiremen);
        btnFiremen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent firemenIntent = new Intent(Intent.ACTION_DIAL);
                firemenIntent.setData(Uri.parse("tel:0173948957"));
                startActivity(firemenIntent);
//                call(btnFiremen);
            }
        });
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new InformativeSection() ,null).commit();
            }
        });

        return view;
    }
//    void call(Button btn) {
//        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//            Intent callIntent = new Intent(Intent.ACTION_CALL);
//            String number = (String) btn.getText();
//            callIntent.setData(Uri.parse("tel:" + number));
//            getActivity().startActivity(callIntent);
//        } else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},
//                    CALL_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == CALL_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//        }
//    }






}