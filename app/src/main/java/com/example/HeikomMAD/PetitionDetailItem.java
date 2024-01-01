package com.example.HeikomMAD;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.Arrays;

@IgnoreExtraProperties
public class PetitionDetailItem {
    private String title;
    private String author;
    private String council;
    private String date;
    private String desc;
    private int target;

    private String userID;
    private Boolean isSuccess;
    private String signedUser;

    private String imagePath;

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCouncil() {
        return council;
    }

    public void setCouncil(String council) {
        this.council = council;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int CurrentSignAmount() {
        return getSignedUserArrayList().size();
    }





    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }



    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getSignedUser() {
        return signedUser;
    }
    public ArrayList<String> getSignedUserArrayList() {
        if (signedUser.equals("[]")) {
            return new ArrayList<>();
        } else {
            String[] signedUserArray = signedUser.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", "").split(" ");
            ArrayList<String> signedUserList = new ArrayList<>(Arrays.asList(signedUserArray));
            return signedUserList;
        }

    }

    public void setSignedUser(String signed_User) {
        signedUser = signed_User;
    }

    public PetitionDetailItem(String title, String author, String date, String council, int target, String desc, String userID, Boolean isSuccess, String signedUser, String imagePath) {

        this.title = title;
        this.author = author;
        this.date = date;
        this.council = council;
        this.target = target;
        this.desc = desc;
        this.userID = userID;
        this.isSuccess = isSuccess;
        this.signedUser = signedUser;
        this.imagePath = imagePath;
    }

    public PetitionDetailItem() {

    }
}
