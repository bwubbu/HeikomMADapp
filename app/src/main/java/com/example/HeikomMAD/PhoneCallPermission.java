package com.example.HeikomMAD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class PhoneCallPermission extends AppCompatActivity {
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_auth);
        setContentView(R.layout.activity_phone_call_permission);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,new InformativeSection(),null);
        fragmentTransaction.commit();



    }
    public void onBackPressed() {
        // Check if the current fragment is HelplineFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment instanceof HelplineSection) {
            // If the current fragment is HelplineFragment, replace it with InformationFragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InformativeSection())
                    .commit();
        } else {
            // If not, proceed with the default back button behavior
            super.onBackPressed();
        }
    }
}