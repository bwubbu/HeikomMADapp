package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class PhoneCallPermission extends AppCompatActivity {
    private static final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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