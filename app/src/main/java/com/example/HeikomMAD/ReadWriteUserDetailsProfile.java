package com.example.HeikomMAD;

public class ReadWriteUserDetailsProfile {
    public String doB,gender,mobile;


    public ReadWriteUserDetailsProfile(){

    }
    public ReadWriteUserDetailsProfile(String textDoB,String textGender,String textmobile){
        this.doB=textDoB;
        this.gender=textGender;
        this.mobile=textmobile;
    }
}