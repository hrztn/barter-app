package com.example.barterapp.model;

import android.net.Uri;

public class UserModelClass {
    String userName;
    String userEmail;
    String userPhoneNo;
    String userAddress;
    String userId;
    String userImage;
    Double totalStars = 0.0;
    int total_reviews = 0;

    public UserModelClass(){

    }

    public UserModelClass(String userName, String userEmail, String userPhoneNo, String userAddress, String userId, String userImage) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNo = userPhoneNo;
        this.userAddress = userAddress;
        this.userId = userId;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public Double getTotalStars() {
        return totalStars;
    }

    public void setTotalStars(Double totalStars) {
        this.totalStars = totalStars;
    }

    public int getTotal_reviews() {
        return total_reviews;
    }

    public void setTotal_reviews(int total_reviews) {
        this.total_reviews = total_reviews;
    }
}
