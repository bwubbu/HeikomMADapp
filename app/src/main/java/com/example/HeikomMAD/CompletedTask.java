package com.example.HeikomMAD;

public class CompletedTask {
    private String taskName;
    private int points;

    public CompletedTask() {
        // Default constructor for Firebase
    }

    public CompletedTask(String taskName, int points) {
        this.taskName = taskName;
        this.points = points;
    }

    // Getters and setters
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
