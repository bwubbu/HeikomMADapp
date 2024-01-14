package com.example.HeikomMAD;

public class taskText{
    String[] taskText = {"Pay your bill","Cleanup the public futsal court", "Cleaning Up the community", "Join the community meet-up", "Decorate Shawn's House"};
    int[] icon = {R.drawable.icon1,R.drawable.icon2,R.drawable.icon2,R.drawable.icon3, R.drawable.icon4};
    //int[] icon2 = {R.drawable.icon1,R.drawable.icon2,R.drawable.icon3, R.drawable.icon3};

    int[] points ={3000,1000,10000, 2000, 350};

    public String[] getTaskText() {
        return taskText;
    }

    public int[] getIcon() {
        return icon;
    }

    /*
    public int[] getIcon2() {
        return icon2;
    }
     */
    public int[] getPoints() {
        return points;
    }
}
