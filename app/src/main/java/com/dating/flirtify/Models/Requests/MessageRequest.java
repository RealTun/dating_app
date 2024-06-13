package com.dating.flirtify.Models.Requests;

public class MessageRequest {
    private int match_id;
    private int receiver_id;
    private String message_content;

    public MessageRequest(int match_id, int receiver_id, String message_content){
        this.match_id = match_id;
        this.receiver_id = receiver_id;
        this.message_content = message_content;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id){
        this.match_id = match_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(int receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }
}
