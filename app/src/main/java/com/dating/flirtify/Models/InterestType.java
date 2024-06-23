package com.dating.flirtify.Models;

public class InterestType {
    private int id;
    private String name_interest_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName_interest_type() {
        return name_interest_type;
    }

    public void setName_interest_type(String name_interest_type) {
        this.name_interest_type = name_interest_type;
    }

    public InterestType(int id, String name_interest_type) {
        this.id = id;
        this.name_interest_type = name_interest_type;
    }
}
