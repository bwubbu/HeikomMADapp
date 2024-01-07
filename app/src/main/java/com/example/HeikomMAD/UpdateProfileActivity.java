package com.example.HeikomMAD;





import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText editTextUpdateName,editTextUpdateDoB,editTextUpdateMobile;
    private RadioGroup radioGroupUpdateGender;
    private RadioButton radioButtonUpdateGenderSelected;
    private String textFullName,textDoB,textGender,textMobile;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;
    private DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);


        progressBar=findViewById(R.id.progressBar);
        editTextUpdateDoB=findViewById(R.id.editText_update_doB);
        editTextUpdateName=findViewById(R.id.editText_update_profile_name);
        editTextUpdateMobile=findViewById(R.id.editText_update_mobile);
        radioGroupUpdateGender=findViewById(R.id.radio_group_update_gender);
        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        //Show profile Data
        showProfile(firebaseUser);

        //Upload profile pic
        Button buttonUploadProfilePic=findViewById(R.id.button_upload_picture);
        buttonUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UpdateProfileActivity.this,UploadProfilePictureActivity.class);
                startActivity(intent);
                finish();

            }
        });
        editTextUpdateDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                picker = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        editTextUpdateDoB.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();


            }
        });

        //Update Profile
        Button buttonUpdateProfile=findViewById(R.id.button_update_profile);
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile(firebaseUser);
            }

            private void updateProfile(FirebaseUser firebaseUser) {
                int selectedGenderID=radioGroupUpdateGender.getCheckedRadioButtonId();
                radioButtonUpdateGenderSelected=findViewById(selectedGenderID);
                textGender=radioButtonUpdateGenderSelected.getText().toString();
                textFullName=editTextUpdateName.getText().toString();
                textDoB=editTextUpdateDoB.getText().toString();
                textMobile=editTextUpdateMobile.getText().toString();

                //Validate Mobile Number using Matcher and Pattern
                String mobileRegex = "^01\\d{8}$";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher=mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(UpdateProfileActivity.this,"Please enter your full name",Toast.LENGTH_LONG).show();
                    editTextUpdateName.setError("Full Name is Required");
                    editTextUpdateName.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(UpdateProfileActivity.this,"Please enter your date of birth",Toast.LENGTH_LONG).show();
                    editTextUpdateDoB.setError("Date of Birth is required");
                    editTextUpdateDoB.requestFocus();
                }else if (TextUtils.isEmpty(radioButtonUpdateGenderSelected.getText())){
                    Toast.makeText(UpdateProfileActivity.this,"Please select your gender",Toast.LENGTH_LONG).show();
                    radioButtonUpdateGenderSelected.setError("Gender is required");
                    radioButtonUpdateGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(UpdateProfileActivity.this,"Please enter your mobile number",Toast.LENGTH_LONG).show();
                    editTextUpdateMobile.setError("Mobile Num is required");
                    editTextUpdateMobile.requestFocus();
                } else if (textMobile.length()!=10) {
                    Toast.makeText(UpdateProfileActivity.this,"Please re-enter your mobile number",Toast.LENGTH_LONG).show();
                    editTextUpdateMobile.setError("Mobile number should be 10 digits");
                    editTextUpdateMobile.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(UpdateProfileActivity.this,"Please re-enter your mobile number",Toast.LENGTH_LONG).show();
                    editTextUpdateMobile.setError("Mobile Number is not valid");
                    editTextUpdateMobile.requestFocus();
                }else {
                    //Obtain data entered by user


                    //Enter User Data into FireBase Realtime database
                    ReadWriteUserDetailsProfile writeUserDetails=new ReadWriteUserDetailsProfile(textDoB,textGender,textMobile,textFullName);
                    //Extract User Reference from Database for "Registered Users"
                    DatabaseReference referenceProfile=FirebaseDatabase.getInstance().getReference("Registered Users");
                    String userID=firebaseUser.getUid();

                    progressBar.setVisibility(View.VISIBLE);
                    referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //Setting new Diplay Name
                                UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                                firebaseUser.updateProfile(profileUpdates);
                                Toast.makeText(UpdateProfileActivity.this,"Update Successful!",Toast.LENGTH_LONG).show();

                                //Stop User from returning to UpdateProfileActivity on pressing back button and close activity
                                Intent intent=new Intent(UpdateProfileActivity.this,UserProfileActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();

                            }else {
                                try {
                                    throw task.getException();
                                }catch (Exception e ){
                                    Toast.makeText(UpdateProfileActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });


                }


            }
        });






    }

    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegistered=firebaseUser.getUid();

        //Extracting user reference from database for Registered Users
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetailsProfile readUserDetails=snapshot.getValue(ReadWriteUserDetailsProfile.class);
                if (readUserDetails!=null){
                    textFullName=readUserDetails.fullName;
                    textDoB=readUserDetails.doB;
                    textGender=readUserDetails.gender;
                    textMobile= readUserDetails.mobile;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateDoB.setText(textDoB);
                    editTextUpdateMobile.setText(textMobile);

                    //Show Gender through Radio button
                    if (textGender.equals("Male")){
                        radioButtonUpdateGenderSelected=findViewById(R.id.radio_Male);

                    }else {
                        radioButtonUpdateGenderSelected=findViewById(R.id.radio_Female);
                    }
                    radioButtonUpdateGenderSelected.setChecked(true);


                }else {
                    Toast.makeText(UpdateProfileActivity.this,"Something went Wrong ! ",Toast.LENGTH_LONG).show();

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this,"Something went Wrong ! ",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}