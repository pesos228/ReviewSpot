package com.webServer.ReviewSpot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "reaction")
public class Reaction extends BaseEntity{
    private Client client;
    private int targetId;
    private String targetType;
    private Boolean isLike;

    protected Reaction() {
    }

    public Reaction(Client client, Review review, Boolean isLike) {
        this.setClient(client);
        this.setTargetId(review.getId());
        this.setTargetType("REVIEW");
        this.setLike(isLike);
    }

    public Reaction(Client client, Comment comment, Boolean isLike) {
        this.setClient(client);
        this.setTargetId(comment.getId());
        this.setTargetType("COMMENT");
        this.setLike(isLike);
    }

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Column(name = "target_id", nullable = false)
    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    @Column(name = "target_type", nullable = false)
    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Column(name = "is_like", nullable = false)
    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }
}
