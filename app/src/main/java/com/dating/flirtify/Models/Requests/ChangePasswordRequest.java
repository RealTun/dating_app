package com.dating.flirtify.Models.Requests;

public class ChangePasswordRequest {
    private String email;
    private String pw;
    public ChangePasswordRequest(String _email, String _pw){
        this.email = _email;
        this.pw = _pw;
    }
}
