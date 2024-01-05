package com.example.HeikomMAD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText registerUserName, registerEmail, registerPassword, registerDateofBirth, registerMobile, registerConfirmPassword;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegGender;
    private RadioButton radioButtonRegGenderSelected;
    private CheckBox checkBoxTermsAndConditions;
    private static final String TAG = "RegisterActivity";
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView regButton = findViewById(R.id.regButton);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LogInActivity
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

        checkBoxTermsAndConditions = findViewById(R.id.radio_button_terms);
        progressBar = findViewById(R.id.progress_bar);
        registerUserName = findViewById(R.id.register_user_name);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerDateofBirth = findViewById(R.id.register_doB);
        radioGroupRegGender=findViewById(R.id.radio_group_reg_gender);
        registerMobile = findViewById(R.id.register_mobile);
        registerConfirmPassword = findViewById(R.id.register_confirm_password);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to LogInActivity
                Intent intent = new Intent(RegisterActivity.this, AuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        registerDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(calendar.MONTH);
                int year = calendar.get(calendar.YEAR);

                picker = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        registerDateofBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        AppCompatButton buttonRegister = findViewById(R.id.register_button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textFullName = registerUserName.getText().toString();
                String textEmail = registerEmail.getText().toString();
                String textPassword = registerPassword.getText().toString();
                String textConfirmPassword = registerConfirmPassword.getText().toString();
                String textDoB = registerDateofBirth.getText().toString();
                int selectedGenderID=radioGroupRegGender.getCheckedRadioButtonId();
                radioButtonRegGenderSelected=findViewById(selectedGenderID);
                String textGender = radioButtonRegGenderSelected.getText().toString();
                String textMobile = registerMobile.getText().toString();

                String mobileRegex = "^01\\d{8}$";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Username", Toast.LENGTH_LONG).show();
                    registerUserName.setError("Username is required");
                    registerUserName.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    registerEmail.setError("Email is required");
                    registerEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    registerEmail.setError("Valid email is required");
                    registerEmail.requestFocus();
                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter your Date of Birth", Toast.LENGTH_LONG).show();
                    registerDateofBirth.setError("Date of Birth is required.");
                    registerDateofBirth.requestFocus();
                }else if (TextUtils.isEmpty(radioButtonRegGenderSelected.getText())){
                    Toast.makeText(RegisterActivity.this,"Please select your gender",Toast.LENGTH_LONG).show();
                    radioButtonRegGenderSelected.setError("Gender is required");
                    radioButtonRegGenderSelected.requestFocus();
                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter your Phone Number", Toast.LENGTH_LONG).show();
                    registerMobile.setError("Phone number is required.");
                    registerMobile.requestFocus();
                } else if (textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please Enter your Phone Number", Toast.LENGTH_LONG).show();
                    registerMobile.setError("Mobile number should be 10 digits.");
                    registerMobile.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(RegisterActivity.this, "Please Enter your Phone Number", Toast.LENGTH_LONG).show();
                    registerMobile.setError("Mobile no. is not valid.");
                    registerMobile.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter your Password", Toast.LENGTH_LONG).show();
                    registerPassword.setError("Password is required.");
                    registerPassword.requestFocus();
                } else if (textPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should at least be 6 digits", Toast.LENGTH_LONG).show();
                    registerPassword.setError("Password is too weak!");
                    registerPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please Confirm your Password", Toast.LENGTH_LONG).show();
                    registerConfirmPassword.setError("Password confirmation is required");
                    registerConfirmPassword.requestFocus();
                } else if (!textPassword.equals(textConfirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Incorrect password confirmation", Toast.LENGTH_LONG).show();
                    registerConfirmPassword.setError("Password Confirmation is required");
                    registerConfirmPassword.requestFocus();
                    //clear entered passwords
                    registerPassword.clearComposingText();
                    registerConfirmPassword.clearComposingText();
                } else if (!checkBoxTermsAndConditions.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Please agree to the terms and conditions", Toast.LENGTH_LONG).show();
                    checkBoxTermsAndConditions.setError("Terms and Conditions Agreement is required");
                    checkBoxTermsAndConditions.requestFocus();
                }else {
                    checkBoxTermsAndConditions.setError(null);
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textPassword, textConfirmPassword, textDoB, textGender, textMobile);

                }
            }
        });
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    //Firebase authentication to to register user with the credentials given
    private void registerUser(String textUserName, String textEmail, String textPassword, String textConfirmPassword, String textDoB, String textGender, String textMobile) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        //create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Update display name of User
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textUserName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    //Enter User Data into Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textUserName, textDoB, textGender, textMobile);

                    //Extracting User reference from db for 'registered users'
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");


                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                //Send Verification Email
                                firebaseUser.sendEmailVerification();

                                //Back to Auth Activity
                                Intent intent = new Intent(RegisterActivity.this, AuthActivity.class);
                                //prevent user from going back to register activity if back button is pressed
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email",
                                        Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        registerPassword.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special characters");
                        registerPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        registerEmail.setError("Your email is invalid or already in user. Kindly re-enter.");
                        registerEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        registerEmail.setError("User is already registered with this email. Use another email.");
                        registerEmail.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}