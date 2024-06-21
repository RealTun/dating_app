package com.dating.flirtify.Models.Requests;

public class RelationshipRequest {
    private int relationship_type_id;

    public int getRelationship_type_id() {
        return relationship_type_id;
    }

    public void setRelationship_type_id(int relationship_type_id) {
        this.relationship_type_id = relationship_type_id;
    }

    public RelationshipRequest(int relationship_type_id) {
        this.relationship_type_id = relationship_type_id;
    }
}
