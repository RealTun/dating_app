package com.dating.flirtify.Models.Requests;

public class LikeRequest {
    private int user2_id;
    private int action;

    public LikeRequest(int user2_id, int action) {
        this.user2_id = user2_id;
        this.action = action;
    }

    public int getId() {
        return user2_id;
    }
    public void setId(int user2_id) {
        this.user2_id = user2_id;
    }
    public int getAction() {
        return action;
    }
    public void setAction(int action) {
        this.action = action;
    }
}