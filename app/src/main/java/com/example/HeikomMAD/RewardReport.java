package com.example.HeikomMAD;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
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

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class RewardReport extends Fragment implements AA_TaskAdapter.TaskCompletionListener, DataManager.ActivitySavedListener {


    //declaring arraylist for the recycler view
    ArrayList<ActivitiesModels> activitiesModels = new ArrayList<>();

    //declaring the icon for the imageView
    ImageView dailyIcon, weeklyIcon;

    private View view; // Declare view as a class-level field

    ArrayList<TaskModel> taskModels = new ArrayList<>();

    //Declare the key for SharedPreferences
    private static final String PREFS_KEY = "FragmentEntryTimestamp";

    private static final String ACHIEVEMENT_TIMESTAMP_KEY = "achievementTimestamp";

    // Declare the adapter as a class-level variable
    private AA_ActivitiesAdapter adapter;


    private CircularProgressBar circularProgressBar;

    DataManager dataManager = DataManager.getInstance();

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
            showAchievementMessage(); // Show the toast
            prefs.edit().putLong(ACHIEVEMENT_TIMESTAMP_KEY, currentTime).apply();
        }
    }
    private void grantPoints() {
        int pointsToAdd = 10; // Change this to the desired number of points
        String userId = "userId"; // Replace this with your user ID retrieval logic

        int currentPoints = PointManager.getPoints(requireContext(), userId); // Get current points
        int updatedPoints = currentPoints + pointsToAdd; // Calculate updated points

        PointManager.setPoints(requireContext(), userId, updatedPoints); // Update points in PointManager
    }

    private void showAchievementMessage() {
        // Show a message, for example, using a Toast
        Toast.makeText(requireContext(), "Daily Login achieved!", Toast.LENGTH_SHORT).show();

        grantPoints(); // Update points
    }

    private void updatePointsDisplay() {
        // Get updated points and display them in the TextView
        int updatedPoints = PointManager.getPoints(requireContext(), "userId");
        TextView pointTextView = view.findViewById(R.id.redeemPoints);
        pointTextView.setText(String.valueOf(updatedPoints));
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_reward_report, container, false);
        view = inflater.inflate(R.layout.fragment_reward_report, container, false);

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

        // Set the initial points to the TextView
        pointTextView.setText(String.valueOf(userPoints[0]));
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updatePointsDisplay(); // Call the method here or in onViewCreated()
    }


    public float getProgress(int completedTasksCount) {
        // Update the circular progress bar or perform other actions
        int totalTasks = activitiesModels.size();
        float progress = (completedTasksCount / totalTasks) * 100;
        return progress;
    }

    public void redirectTextRedeem(TextView textView) {
        textView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.rewardRedeem));
    }


}