package com.example.HeikomMAD;

import android.content.Context;
import android.content.SharedPreferences;

public class PointManager {

    private static final String POINTS_KEY = "user_points";

    // Method to get points for a specific user
    public static int getPoints(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPoints", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(userId + POINTS_KEY, 0); // Default value is 0
    }

    // Method to add points to a specific user
    public static void addPoints(Context context, String userId, int pointsToAdd) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPoints", Context.MODE_PRIVATE);
        int currentPoints = getPoints(context, userId);
        int updatedPoints = currentPoints + pointsToAdd;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(userId + POINTS_KEY, updatedPoints);
        editor.apply();
    }

    // Method to deduct points from a specific user (if needed)
    public static boolean deductPoints(Context context, String userId, int pointsToDeduct) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPoints", Context.MODE_PRIVATE);
        int currentPoints = getPoints(context, userId);
        int updatedPoints = Math.max(0, currentPoints - pointsToDeduct);

        if (updatedPoints != currentPoints) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(userId + POINTS_KEY, updatedPoints);
            editor.apply();
            return true; // Deduction successful
        } else {
            return false; // Insufficient points, deduction failed
        }
    }

    public static void setPoints(Context context, String userId, int points) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPoints", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(userId + POINTS_KEY, points);
        editor.apply();
    }
}