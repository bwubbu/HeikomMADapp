package com.example.HeikomMAD;

public class ReadWriteUserDetailsProfile {
    public String doB,gender,mobile,fullName;


    public ReadWriteUserDetailsProfile(){

    }
    public ReadWriteUserDetailsProfile(String textDoB,String textGender,String textmobile,String fullname){
        this.doB=textDoB;
        this.gender=textGender;
        this.mobile=textmobile;
        this.fullName=fullname;
    }
}