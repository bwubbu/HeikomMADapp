package com.example.HeikomMAD;

import java.util.ArrayList;

public class PetitionApprovalItem {
    private String title;
    private String author;
    private String council;
    private String date;
    private String desc;
    private int target;
    private String petitionID;
    private String image;
    private String userID;
    private String status;
    private Boolean isSuccess;
    private ArrayList<String> signedUser;


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

    public int getCurrentSign() {
        return getSigned_User().size();
    }


    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public ArrayList<String> getSigned_User() {
        return signedUser;
    }

    public void setSigned_User(ArrayList<String> signed_User) {
        signedUser = signed_User;
    }

    public PetitionApprovalItem(String petitionID, String title, String author, String date, String council, int target, String desc, String image, String userID, String status, Boolean isSuccess, ArrayList<String> signedUser)  {
        this.petitionID = petitionID;
        this.title = title;
        this.author = author;
        this.date = date;
        this.council = council;
        this.target = target;
        this.desc = desc;
        this.image = image;
        this.userID = userID;
        this.status = status;
        this.isSuccess = isSuccess;
        this.signedUser = signedUser;
    }

    public PetitionApprovalItem() {
        this.petitionID = "";
        this.title = "";
        this.author = "";
        this.date = "";
        this.council = "";
        this.target = 0;
        this.desc = "";
        this.image = "";
        this.userID = "";
        this.status = "";
        this.isSuccess = false;
        this.signedUser = new ArrayList<>();
    }
}