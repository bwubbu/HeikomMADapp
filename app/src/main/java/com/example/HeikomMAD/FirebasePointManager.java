package com.example.HeikomMAD;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class FirebasePointManager {

    private DatabaseReference pointsRef;

    public FirebasePointManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        pointsRef = database.getReference("Points");
    }

    public void saveUserReward(String userId, UserReward userReward) {
        pointsRef.child(userId).setValue(userReward);
    }

    /*
    public void fetchPointsForUser(String userId, PointFetchListener listener) {
        pointsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    UserReward userReward = dataSnapshot.getValue(UserReward.class);
                    if (userReward != null) {
                        listener.onPointFetchSuccess(userReward.getPoints());
                    } else {
                        listener.onPointFetchFailure("User data is null");
                    }
                } else {
                    listener.onPointFetchFailure("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onPointFetchFailure(databaseError.getMessage());
            }
        });
    }

     */

    public void fetchPointsForUser(String userId, PointFetchListener listener) {
        pointsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (data != null && data.containsKey("points")) {
                        int points = (int) (long) data.get("points"); // Cast to long first, then to int
                        listener.onPointFetchSuccess(points);
                    } else {
                        listener.onPointFetchFailure("User data is null");
                    }
                } else {
                    listener.onPointFetchFailure("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onPointFetchFailure(databaseError.getMessage());
            }
        });
    }


    public void incrementTasksDone(String userId) {
        pointsRef.child(userId).child("tasksDone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer currentTasksDone = dataSnapshot.getValue(Integer.class);
                if (currentTasksDone == null) currentTasksDone = 0;
                pointsRef.child(userId).child("tasksDone").setValue(currentTasksDone + 1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public interface PointFetchListener {
        void onPointFetchSuccess(int points);
        void onPointFetchFailure(String message);
    }

    public void initializeUserDataIfNotExists(FirebaseUser firebaseUser, Context context) {
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            String userName = firebaseUser.getDisplayName(); // Get the user's name

            pointsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // Initialize with the user's name, 0 points, and 0 tasks done
                        UserReward newUserReward = new UserReward(userName, 0, 0);
                        newUserReward.setCompletedTasks(new ArrayList<>()); // Initialize empty list of completed tasks
                        pointsRef.child(userId).setValue(newUserReward);
                    }
                    // Existing data handling...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "Error: Can't initialize database", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void addPointsToUser(String userId, int additionalPoints, PointUpdateListener listener) {
        pointsRef.child(userId).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer currentPoints = dataSnapshot.getValue(Integer.class);
                    if (currentPoints == null) currentPoints = 0;
                    pointsRef.child(userId).child("points").setValue(currentPoints + additionalPoints)
                            .addOnSuccessListener(aVoid -> listener.onPointUpdateSuccess())
                            .addOnFailureListener(e -> listener.onPointUpdateFailure(e.getMessage()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onPointUpdateFailure(databaseError.getMessage());
            }
        });
    }

    public void deductPointsFromUser(String userId, int pointsToDeduct, PointUpdateListener listener) {
        pointsRef.child(userId).child("points").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer currentPoints = dataSnapshot.getValue(Integer.class);
                    if (currentPoints == null) currentPoints = 0;
                    if (currentPoints >= pointsToDeduct) {
                        pointsRef.child(userId).child("points").setValue(currentPoints - pointsToDeduct)
                                .addOnSuccessListener(aVoid -> listener.onPointUpdateSuccess())
                                .addOnFailureListener(e -> listener.onPointUpdateFailure(e.getMessage()));
                    } else {
                        listener.onPointUpdateFailure("Insufficient points");
                    }
                } else {
                    listener.onPointUpdateFailure("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onPointUpdateFailure(databaseError.getMessage());
            }
        });
    }

    public void addCompletedTaskToUser(String userId, CompletedTask task, PointUpdateListener listener) {
        DatabaseReference userTasksRef = pointsRef.child(userId).child("completedTasks");
        userTasksRef.push().setValue(task) // push() creates a new list item
                .addOnSuccessListener(aVoid -> listener.onPointUpdateSuccess())
                .addOnFailureListener(e -> listener.onPointUpdateFailure(e.getMessage()));
    }



    public void fetchCompletedTasks(String userId, FirebasePointManager.ActivitiesFetchListener listener) {
        DatabaseReference userActivitiesRef = pointsRef.child(userId).child("completedTasks");
        userActivitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assume each child in this snapshot is a HashMap representation of a CompletedTask
                    HashMap<String, HashMap<String, Object>> tasksMap = (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();

                    ArrayList<CompletedTask> tasks = new ArrayList<>();
                    if (tasksMap != null) {
                        for (HashMap<String, Object> taskData : tasksMap.values()) {
                            // Extract data for each task and create CompletedTask objects
                            String taskName = taskData.containsKey("taskName") ? (String) taskData.get("taskName") : "";
                            long points = taskData.containsKey("points") ? (long) taskData.get("points") : 0;
                            CompletedTask task = new CompletedTask(taskName, (int) points);
                            tasks.add(task);
                        }
                    }
                    listener.onActivitiesFetchSuccess(tasks);
                } else {
                    listener.onActivitiesFetchFailure("No completed tasks found for user");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onActivitiesFetchFailure(databaseError.getMessage());
            }
        });
    }

    public void fetchCompletedTaskCount(String userId, CompletedTaskCountListener listener) {
        DatabaseReference userActivitiesRef = pointsRef.child(userId).child("completedTasks");
        userActivitiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int completedTaskCount = (int) dataSnapshot.getChildrenCount();
                    listener.onCompletedTaskCountFetchSuccess(completedTaskCount);
                } else {
                    listener.onCompletedTaskCountFetchFailure("No completed tasks found for user");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCompletedTaskCountFetchFailure(databaseError.getMessage());
            }
        });
    }

    public interface CompletedTaskCountListener {
        void onCompletedTaskCountFetchSuccess(int taskCount);
        void onCompletedTaskCountFetchFailure(String message);
    }

    //daily login for user
    public void checkAndAddDailyPoints(String userId, PointUpdateListener listener) {
        pointsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                    long lastLoginTime = 0;
                    if (data.containsKey("lastLoginTime")) {
                        lastLoginTime = (long) data.get("lastLoginTime");
                    }

                    long currentTime = System.currentTimeMillis();
                    long hoursElapsed = TimeUnit.MILLISECONDS.toHours(currentTime - lastLoginTime);

                    if (hoursElapsed >= 24) {
                        int currentPoints = data.containsKey("points") ? (int) (long) data.get("points") : 0;
                        pointsRef.child(userId).child("points").setValue(currentPoints + 10); // Add 10 points
                        pointsRef.child(userId).child("lastLoginTime").setValue(currentTime); // Update last login time

                        // Add to completed tasks
                        CompletedTask dailyLoginTask = new CompletedTask("Daily login", 10);
                        addCompletedTaskToUser(userId, dailyLoginTask, listener);
                    } else {
                        listener.onPointUpdateFailure("24 hours have not passed yet");
                    }
                } else {
                    listener.onPointUpdateFailure("User data not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onPointUpdateFailure(databaseError.getMessage());
            }
        });
    }





    private ArrayList<ActivitiesModels> convertToActivitiesModels(ArrayList<CompletedTask> completedTasks) {
        ArrayList<ActivitiesModels> activities = new ArrayList<>();
        for (CompletedTask task : completedTasks) {
            activities.add(new ActivitiesModels(task.getTaskName(), String.valueOf(task.getPoints())));
        }
        return activities;
    }

    public interface ActivitiesFetchListener {
        void onActivitiesFetchSuccess(ArrayList<CompletedTask> tasks);
        void onActivitiesFetchFailure(String message);
    }

    public interface PointUpdateListener {
        void onPointUpdateSuccess();
        void onPointUpdateFailure(String message);
    }
}
