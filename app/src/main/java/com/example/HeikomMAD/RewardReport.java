package com.example.HeikomMAD;

import android.content.Context;
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


    private CircularProgressBar circularProgressBar;

    DataManager dataManager = DataManager.getInstance();

    FirebaseUser firebaseUser;

    private TextView headerUser;

    private String username;

    private ImageView headerProfilepic;

    FirebasePointManager firebasePointManager = new FirebasePointManager();

    @Override
    public void onResume() {

        super.onResume();
    }


    private void checkAndShowDailyLoginToast() {
        SharedPreferences prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        long lastToastTime = prefs.getLong(ACHIEVEMENT_TIMESTAMP_KEY, 0);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastToastTime;
        if (elapsedTime > TimeUnit.HOURS.toMillis(24)) {
            // Award 10 points for daily login
            awardDailyLoginPoints();
            prefs.edit().putLong(ACHIEVEMENT_TIMESTAMP_KEY, currentTime).apply();
        }
    }

    private void awardDailyLoginPoints() {
        int pointsToAdd = 10; // Points to add for daily login
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Add points to the user in Firebase
            firebasePointManager.addPointsToUser(userId, pointsToAdd, new FirebasePointManager.PointUpdateListener() {
                @Override
                public void onPointUpdateSuccess() {
                    Toast.makeText(requireContext(), "Daily Login achieved! " + pointsToAdd + " points awarded.", Toast.LENGTH_SHORT).show();
                    //updatePointsDisplay();
                }

                @Override
                public void onPointUpdateFailure(String message) {
                    Toast.makeText(requireContext(), "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /*
    private void updatePointsDisplay() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            firebasePointManager.fetchPointsForUser(userId, new FirebasePointManager.PointFetchListener() {
                @Override
                public void onPointFetchSuccess(int points) {
                    TextView pointTextView = view.findViewById(R.id.redeemPoints);
                    pointTextView.setText(String.valueOf(points));
                }

                @Override
                public void onPointFetchFailure(String message) {
                    Log.e("RewardReport", "Failed to fetch points: " + message);
                }
            });
        }
    }
    */



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reward_report, container, false);
        headerUser = view.findViewById(R.id.headerUser);
        headerProfilepic = view.findViewById(R.id.headerProfilepic); // Initialize ImageView


        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewPoints);

        activitiesModels = DataManager.getInstance().getClickedTask();
        for(int i=0;i<activitiesModels.size();i++){
            System.out.println(activitiesModels);
        }
        adapter = new AA_ActivitiesAdapter(requireContext(), activitiesModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        TextView pointTextView = view.findViewById(R.id.redeemPoints);
        ImageButton pointButton = view.findViewById(R.id.rewardReportButton);
        final int[] userPoints = { PointManager.getPoints(requireContext(), "userId") };

        // Get the current logged-in Firebase user
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        // If the user is logged in, show the user profile
        if (firebaseUser != null) {
            showUserProfile(firebaseUser);
            String userId = firebaseUser.getUid();

            // Fetch the user's points from the database
            firebasePointManager.fetchPointsForUser(userId, new FirebasePointManager.PointFetchListener() {
                @Override
                public void onPointFetchSuccess(int points) {
                    // Set the text of the TextView with the fetched points
                    pointTextView.setText(String.valueOf(points));
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



        //Making icon clickbale redirect to another fragment
        dailyIcon = view.findViewById(R.id.dailyIcon);
        dailyIcon.setOnClickListener(v -> {
            // Set the background color of the ImageView to transparent
            dailyIcon.setBackgroundColor(Color.TRANSPARENT);
            // Change the background color temporarily
            int originalColor = ((ColorDrawable) dailyIcon.getBackground()).getColor();
            dailyIcon.setBackgroundColor(Color.parseColor("#f2d37c")); // Change this to your desired color
            new Handler().postDelayed(() -> dailyIcon.setBackgroundColor(originalColor), 200); // Change back after a delay

            // Navigate to another fragment using Navigation Component
            Navigation.findNavController(view).navigate(R.id.rewardTask);
        });


        weeklyIcon = view.findViewById(R.id.weeklyIcon);
        weeklyIcon.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate((R.id.rewardTask));
        });

        // Fetch the completed tasks count
        int completedTasksCount = dataManager.getCompletedTasksCount(getContext());
        int taskCount = dataManager.getTaskCount();
        System.out.println("Task Count:" + completedTasksCount);
        System.out.println("Model Size:" + taskModels.size());

        circularProgressBar = view.findViewById(R.id.weeklyProgressBar);
        circularProgressBar.setProgress(completedTasksCount);
        circularProgressBar.setProgressMax(taskCount);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataManager.getInstance().setActivitySavedListener(this); // Set this fragment as the observer for activity saved events
        checkAndShowDailyLoginToast(); // Check for daily login achievements
    }

    @Override
    public void onActivitySaved() {
        activitiesModels.clear(); // Clear the current list of activities
        activitiesModels.addAll(DataManager.getInstance().getClickedTask()); // Get the latest activities
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Notify adapter of data changes
        }
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
                ReadWriteUserDetailsProfile readUserDetails = snapshot.getValue(ReadWriteUserDetailsProfile.class);
                if (readUserDetails != null) {
                    username = firebaseUser.getDisplayName();
                    headerUser.setText("Welcome, " + username + "!");
                    Uri uri = firebaseUser.getPhotoUrl();
                    if (uri != null && headerProfilepic != null && getActivity() != null) {
                        Picasso.with(getActivity()).load(uri).into(headerProfilepic);
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }









}