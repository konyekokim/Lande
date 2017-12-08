package com.konye.lande;

/**
 * Created by KONYE on 10/9/2017.
 */

public class AppVerClass {
    private String nameTextView;
    private int verificationStatus;
    public AppVerClass(String nameTextView, int verificationStatus){
        this.nameTextView = nameTextView;
        this.verificationStatus = verificationStatus;
    }

    public String getNameTextView(){
        return nameTextView;
    }
    public int getVerificationStatus(){
        return verificationStatus;
    }
}
