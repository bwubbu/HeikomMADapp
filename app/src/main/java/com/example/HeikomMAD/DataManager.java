package com.example.HeikomMAD;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataManager {

    private int taskCount = 0;
    private int completedTaskCount = 0;

    ArrayList<ActivitiesModels> clickedTask = new ArrayList<>();
    private static DataManager instance;
    private static final String PREF_COMPLETED_TASKS = "completed_tasks";

    private ActivitySavedListener activitySavedListener;

    private Set<String> savedActivityIds = new HashSet<>();

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void saveTotalTask(int taskCount) {
        this.taskCount = taskCount;
    }

    public int getTaskCount() {
        return this.taskCount;
    }

    public void saveCompletedTasksCount(Context context, int completedTasksCount) {
        SharedPreferences.Editor editor = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE).edit();
        editor.putInt(PREF_COMPLETED_TASKS, completedTasksCount);
        editor.apply();
        this.completedTaskCount = completedTasksCount;
    }

    public int getCompletedTasksCount(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getInt(PREF_COMPLETED_TASKS, 0); // Default value is 0 if not found
        //return this.completedTaskCount;
    }

    public void setActivitySavedListener(ActivitySavedListener listener) {
        this.activitySavedListener = listener;
    }

    public void saveActivity(String activity, String points) {
        this.clickedTask.add(new ActivitiesModels(activity, points));
        savedActivityIds.add(activity); // Add activity ID to the set

        if (activitySavedListener != null) {
            activitySavedListener.onActivitySaved(); // Notify observers
        }
    }


    public boolean isActivityAlreadySaved(String activityText) {
        return savedActivityIds.contains(activityText);
    }

    public ArrayList<ActivitiesModels> getClickedTask() {
        return clickedTask;
    }


    public interface ActivitySavedListener {
        void onActivitySaved();
    }
}