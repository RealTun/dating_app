package com.dating.flirtify.Models.Requests;

public class UserRequest {
    private String fullname;
    private String bio;
    private int gender;

    public UserRequest(String fullname, String bio, int gender) {
        this.fullname = fullname;
        this.bio = bio;
        this.gender = gender;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
