package com.example.HeikomMAD;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.squareup.picasso.Picasso;

public class RewardReport extends Fragment implements AA_TaskAdapter.TaskCompletionListener, DataManager.ActivitySavedListener {


    //declaring arraylist for the recycler view
    ArrayList<ActivitiesModels> activitiesModels = new ArrayList<>();

    //declaring the icon for the imageView
    ImageView dailyIcon, weeklyIcon;

    private View view; // Declare view as a class-level field

    ArrayList<TaskModel> taskModels = new ArrayList<>();

    private static final String PREFS_KEY = "FragmentEntryTimestamp";

    private static final String ACHIEVEMENT_TIMESTAMP_KEY = "achievementTimestamp";

    private AA_ActivitiesAdapter adapter;

    AA_ActivitiesAdapter activitiesAdapter = new AA_ActivitiesAdapter(getContext(), new ArrayList<>());



    private CircularProgressBar circularProgressBar;

    //DataManager dataManager = DataManager.getInstance();

    FirebaseUser firebaseUser;

    private TextView headerUser;

    private String username;

    private ImageView headerProfilepic;

    FirebasePointManager firebasePointManager = new FirebasePointManager();

    @Override
    public void onResume() {

        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward_report, container, false);
        headerUser = view.findViewById(R.id.headerUser);
        headerProfilepic = view.findViewById(R.id.headerReward); // Initialize ImageView

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPoints);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the adapter with an empty list
        activitiesAdapter = new AA_ActivitiesAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(activitiesAdapter);


        //declare fpm
        FirebasePointManager firebasePointManager = new FirebasePointManager();
        TextView pointTextView = view.findViewById(R.id.redeemPoints);
        ImageButton pointButton = view.findViewById(R.id.rewardReportButton);

        // Get the current logged-in Firebase user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //getting tutorial image
        ImageView tutorialImageView = view.findViewById(R.id.tutorialImageView);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        boolean hasSeenTutorial = sharedPreferences.getBoolean("hasSeenTutorial", false);
        if (!hasSeenTutorial) {
            tutorialImageView.setVisibility(View.VISIBLE);
            tutorialImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tutorialImageView.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("hasSeenTutorial", true);
                    editor.apply();
                }
            });
        }


        // If the user is logged in, show the user profile
        if (firebaseUser != null) {
            showUserProfile(firebaseUser);

            // Initialize user data if it doesn't exist
            firebasePointManager.initializeUserDataIfNotExists(firebaseUser, getContext());

            String userId = firebaseUser.getUid();

            //calling the activites from the database
            firebasePointManager.fetchCompletedTasks(firebaseUser.getUid(), new FirebasePointManager.ActivitiesFetchListener() {
                @Override
                public void onActivitiesFetchSuccess(ArrayList<CompletedTask> tasks) {
                    activitiesAdapter.updateActivities(tasks);
                }

                @Override
                public void onActivitiesFetchFailure(String message) {
                    Log.e("YourFragmentOrActivity", "Error fetching completed tasks: " + message);
                    // Handle the error case
                }
            });

            firebasePointManager.fetchPointsForUser(userId, new FirebasePointManager.PointFetchListener() {
                @Override
                public void onPointFetchSuccess(int points) {
                    pointTextView.setText(String.valueOf(points) + " Points"); // Update TextView
                }

                @Override
                public void onPointFetchFailure(String message) {
                    Log.e("RewardReport", "Failed to fetch points: " + message);
                }
            });




        }

        View.OnClickListener OCLpoint = new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Navigation.findNavController(view).navigate(R.id.rewardTask);
            }
        };
        pointButton.setOnClickListener(OCLpoint);

        //Making the textview for the points to be clickable and redirect it to redeem
        TextView redeemTV = view.findViewById(R.id.redeemPoints);
        redirectTextRedeem(redeemTV);


        //Making icon clickable for daily reward
        // Inside onCreateView or onViewCreated of RewardReport fragment

        dailyIcon = view.findViewById(R.id.dailyIcon);
        dailyIcon.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            firebasePointManager.checkAndAddDailyPoints(userId, new FirebasePointManager.PointUpdateListener() {
                @Override
                public void onPointUpdateSuccess() {
                    Toast.makeText(getContext(), "Daily points added!", Toast.LENGTH_SHORT).show();

                    // Fetch the updated list of completed tasks
                    firebasePointManager.fetchCompletedTasks(firebaseUser.getUid(), new FirebasePointManager.ActivitiesFetchListener() {
                        @Override
                        public void onActivitiesFetchSuccess(ArrayList<CompletedTask> tasks) {
                            activitiesAdapter.updateActivities(tasks); // Update the adapter with new data
                        }

                        @Override
                        public void onActivitiesFetchFailure(String message) {
                            Log.e("RewardReport", "Error fetching completed tasks: " + message);
                        }
                    });
                }

                @Override
                public void onPointUpdateFailure(String message) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            });
        });




        weeklyIcon = view.findViewById(R.id.weeklyIcon);
        weeklyIcon.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate((R.id.rewardTask));
        });

        // Fetch the completed tasks count
        int completedTasksCount =  1;//firebasePointManager.fetchCompletedTasks(firebaseUser,);
        System.out.println("Task Count:" + completedTasksCount);
        System.out.println("Model Size:" + taskModels.size());

        circularProgressBar = view.findViewById(R.id.weeklyProgressBar);
        circularProgressBar.setProgress(completedTasksCount);
        circularProgressBar.setProgressMax(5);

        //back button to the homepage
        ImageView backButton = view.findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent intent = new Intent(getActivity(), HomePageActivity.class);
                startActivity(intent);
            }

        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManager.getInstance().setActivitySavedListener(this); // Set this fragment as the observer for activity saved events
    }


    @Override
    public void onActivitySaved() {
        //empty implementation
    }


    public void redirectTextRedeem(TextView textView) {
        textView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.rewardRedeem));
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

                    Picasso.with(getContext()).load(uri).into(headerProfilepic);

                } else {
                    Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


}