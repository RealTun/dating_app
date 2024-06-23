package com.dating.flirtify.Models.Requests;

import java.util.List;

public class InterestRequest {
    private List<Integer> array_interest_id;

    public InterestRequest(List<Integer> array_interest_id) {
        this.array_interest_id = array_interest_id;
    }

    public List<Integer> getInterest_ids() {
        return array_interest_id;
    }

    public void setInterest_ids(List<Integer> array_interest_id) {
        this.array_interest_id = array_interest_id;
    }
}
