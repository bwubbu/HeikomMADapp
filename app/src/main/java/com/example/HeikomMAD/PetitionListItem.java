package com.example.HeikomMAD;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class PetitionListItem {
    private String title;
    private String desc;
    private String author;
    private String petitionID;

    private String imagePath;

    public PetitionListItem(){
    }
    public PetitionListItem(String title, String desc, String author, String petitionID, String imagePath) {
        this.title = title;
        this.desc = desc;
        this.author = author;
        this.petitionID = petitionID;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getAuthor() {
        return author;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getPetitionID() {
        return petitionID;
    }

    public void setPetitionID(String petitionID) {
        this.petitionID = petitionID;
    }
}
