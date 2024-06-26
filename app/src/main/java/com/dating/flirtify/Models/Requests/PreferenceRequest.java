package com.dating.flirtify.Models.Requests;

public class PreferenceRequest {
    private int min_age;
    private int max_age;
    private int max_distance;

    public PreferenceRequest(int min_age, int max_age, int max_distance) {
        this.min_age = min_age;
        this.max_age = max_age;
        this.max_distance = max_distance;
    }

    public int getMin_age() {
        return min_age;
    }

    public void setMin_age(int min_age) {
        this.min_age = min_age;
    }

    public int getMax_age() {
        return max_age;
    }

    public void setMax_age(int max_age) {
        this.max_age = max_age;
    }

    public int getMax_distance() {
        return max_distance;
    }

    public void setMax_distance(int max_distance) {
        this.max_distance = max_distance;
    }
}
