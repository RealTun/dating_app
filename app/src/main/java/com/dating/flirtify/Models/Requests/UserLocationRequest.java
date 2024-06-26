package com.dating.flirtify.Models.Requests;

public class UserLocationRequest {
    private String location;

    public UserLocationRequest(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
