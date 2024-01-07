package com.example.HeikomMAD;

public class UserReward {

    private String name;
    private int points; // Changed from Map to int
    private int tasksDone;

    public UserReward() {
        // Default constructor required for Firebase
    }

    public UserReward(String name, int points, int tasksDone) {
        this.name = name;
        this.points = points;
        this.tasksDone = tasksDone;
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
}
