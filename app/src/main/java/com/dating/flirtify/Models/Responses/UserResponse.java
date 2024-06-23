package com.dating.flirtify.Models.Responses;

import java.util.List;

public class UserResponse {
    private int id;
    private String fullname;
    private String bio;
    private int age;
    private int gender;
    private int looking_for;
    private String location;
    private List<String> interests;
    private String relationship;
    private List<String> photos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getLooking_for() {
        return looking_for;
    }

    public void setLooking_for(int looking_for) {
        this.looking_for = looking_for;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationships(String relationship) {
        this.relationship = relationship;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getAvatar() {
        return photos.get(0);
    }
}

