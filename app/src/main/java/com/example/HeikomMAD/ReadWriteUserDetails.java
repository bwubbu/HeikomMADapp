package com.example.HeikomMAD;

public class ReadWriteUserDetails {
    public String userName, doB, gender, mobile;

    public ReadWriteUserDetails() {
    }

    public ReadWriteUserDetails(String textDoB, String textGender, String textMobile, String textUserName) {
        this.doB = textDoB;
        this.gender = textGender;
        this.mobile = textMobile;
        this.userName = textUserName;
    }
}
