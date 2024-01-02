package com.example.HeikomMAD;

public class ActivitiesModels {
    String activityRep, pointsRep;

    int destinationId;


    public ActivitiesModels(String activity, String points) {
        this.activityRep = activity;
        this.pointsRep = points;
        destinationId = 1;
    }

    public String getActivity() {
        return activityRep;
    }

    public String getPoints() {
        return pointsRep;
    }

    public int getDestinationId() {
        return destinationId;
    }
}
