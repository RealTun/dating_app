package com.dating.flirtify.Models;

public class User {
    private int id;
    private String email;
    private String fullname;
    private String phone;
    private String bio;
    private int age;
    private int gender;

    private int looking_for;
    private String location;
    private String confirmation_code;
    private String confirmation_time;

    // Constructors
    public User(String fullname, int age) {
        this.fullname = fullname;
        this.age = age;
    }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public int getLookingFor() {
        return looking_for;
    }

    public void setLookingFor(int looking_for) {
        this.looking_for = looking_for;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getConfirmationCode() {
        return confirmation_code;
    }

    public void setConfirmationCode(String confirmation_code) {
        this.confirmation_code = confirmation_code;
    }

    public String getConfirmationTime() {
        return confirmation_time;
    }

    public void setConfirmationTime(String confirmation_time) {
        this.confirmation_time = confirmation_time;
    }
}
