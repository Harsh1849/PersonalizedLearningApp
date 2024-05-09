package com.quizapp;

public class ModelClass {

    public String UserName;
    String Email;
    String PhoneNumber;
    String Password;
    String imageUrl;

    public ModelClass(String userName, String email, String phoneNumber, String password, String imageUrl) {
        UserName = userName;
        Email = email;
        Password = password;
        PhoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = PhoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
