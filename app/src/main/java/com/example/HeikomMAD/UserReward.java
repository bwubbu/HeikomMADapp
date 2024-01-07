package com.example.HeikomMAD;

import java.util.ArrayList;

public class UserReward {

    private String name;
    private int points; // Changed from Map to int
    private int tasksDone;

    private ArrayList<CompletedTask> completedTasks;

    public UserReward() {
        // Default constructor for Firebase
        completedTasks = new ArrayList<>();
    }

    public UserReward(String name, int points, int tasksDone) {
        this.name = name;
        this.points = points;
        this.tasksDone = tasksDone;
        this.completedTasks = new ArrayList<>();
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTasksDone() {
        return tasksDone;
    }

    public void setTasksDone(int tasksDone) {
        this.tasksDone = tasksDone;
    }

    public ArrayList<CompletedTask> getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(ArrayList<CompletedTask> completedTasks) {
        this.completedTasks = completedTasks;
    }

    public void addCompletedTask(CompletedTask task) {
        completedTasks.add(task);
    }
}
