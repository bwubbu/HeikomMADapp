package com.example.HeikomMAD;

public class CardModel {
    int couponImage;
    String couponText;
    String couponDesc;

    boolean isClicked;

    //points
    int points;

    private float alphaValue;


    public boolean isClicked() {
        return isClicked;
    }

    public CardModel(int couponImage, String couponText, String couponDesc, int points) {
        this.couponImage = couponImage;
        this.couponText = couponText;
        this.couponDesc = couponDesc;
        this.points = points;
        this.isClicked = false;
        this.alphaValue = 1.0f;
    }

    public int getCouponImage() {
        return couponImage;
    }

    public String getCouponText() {
        return couponText;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public int getPoints() {
        return points;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public float getAlphaValue() {
        return alphaValue;
    }

    public void setAlphaValue(float alpha) {
        alphaValue = alpha;
    }
}

