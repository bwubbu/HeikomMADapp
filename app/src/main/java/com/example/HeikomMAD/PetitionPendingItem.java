package com.example.HeikomMAD;

public class PetitionPendingItem {
    private String title;
    private String author;
    private String petitionID;

    public PetitionPendingItem(String title,  String author, String petitionID) {
        this.title = title;
        this.author = author;
        this.petitionID = petitionID;
    }

    public String getAuthor() {
        return author;
    }


    public String getTitle() {
        return title;
    }

    public String getPetitionID() {
        return petitionID;
    }
}