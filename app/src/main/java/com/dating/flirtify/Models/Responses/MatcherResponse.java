package com.dating.flirtify.Models.Responses;

import java.io.Serializable;

public class MatcherResponse implements Serializable {
    private int matcher_id;
    private int match_id;
    private String fullname;
    private String imageUrl;
    private String last_message;

//    public MatcherResponse(int _matcher_id, int _match_id, String _fullname, String _imageUrl, String _last_message){
//        this.matcher_id = _matcher_id;
//        this.match_id = _match_id;
//        this.fullname = _fullname;
//        this.imageUrl = _imageUrl;
//        this.last_message = _last_message;
//    }

    public int getMatcher_id() {
        return matcher_id;
    }

    public void setMatcher_id(int matcher_id) {
        this.matcher_id = matcher_id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }
}
