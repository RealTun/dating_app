package com.dating.flirtify.Models.Responses;

public class MessageResponse {
    private int id;
    private int match_id;
    private int sender_id;
    private int receiver_id;
    private String message_content;
    private String time_sent;
    private String imageReceiverUrl;
    private boolean isSentByCurrentUser;

    public int getId() {
        return id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public String getTime_sent() {
        return time_sent;
    }

    public String getImageReceiverUrl(){ return imageReceiverUrl; }

    public void setImageReceiverUrl(String imageReceiverUrl) {
        this.imageReceiverUrl = imageReceiverUrl;
    }

    public boolean isSentByCurrentUser() {
        return isSentByCurrentUser;
    }

    public void setSentByCurrentUser(boolean sentByCurrentUser) {
        isSentByCurrentUser = sentByCurrentUser;
    }
}
