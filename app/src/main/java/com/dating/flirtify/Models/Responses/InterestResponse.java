package com.dating.flirtify.Models.Responses;

import java.util.List;

public class InterestResponse {
    private String user_name;
    private List<String> name_interest;

    public InterestResponse(String user_name, List<String> name_interest) {
        this.user_name = user_name;
        this.name_interest = name_interest;
    }
    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<String> getName_interest() {
        return name_interest;
    }

    public void setName_interest(List<String> name_interest) {
        this.name_interest = name_interest;
    }
}
