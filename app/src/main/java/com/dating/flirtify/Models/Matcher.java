package com.dating.flirtify.Models;

public class Matcher {
    private final int id;
    private final String imageUrl;
    private final String fullname;


    public Matcher(int id, String image, String name) {
        this.id = id;
        this.imageUrl = image;
        this.fullname = name;
    }

    public String getImage() {
        return imageUrl;
    }

    public String getName() {
        return fullname;
    }

    public int getId() {
        return id;
    }
}
