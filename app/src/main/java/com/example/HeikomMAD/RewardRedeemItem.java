package com.example.HeikomMAD;

public class RewardRedeemItem {
    String imageName;
    String textPoint;
    String textDescription;

    public RewardRedeemItem(String imageName, String textPoint, String textDescription) {
        this.imageName = imageName;
        this.textPoint = textPoint;
        this.textDescription = textDescription;
    }

    public String getImageName() {
        return imageName;
    }

    public String getTextPoint() {
        return textPoint;
    }

    public String getTextDescription() {
        return textDescription;
    }
}
