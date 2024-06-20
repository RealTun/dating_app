package com.dating.flirtify.Models.Requests;

import java.util.Date;

public class RegisterRequest {
    private String email;
    private String pw;
    private String fullname;
    private String bio;
    private int age;
    private int gender;
    private int looking_for;
    private int relationship_type;
    private String location;

    public RegisterRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(int looking_for) {
        this.looking_for = looking_for;
    }

    public int getRelationship_type() {
        return relationship_type;
    }

    public void setRelationship_type(int relationship_type) {
        this.relationship_type = relationship_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
