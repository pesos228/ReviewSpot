package com.webServer.ReviewSpot.dto;

public class ReactionInputDto {

    private int clientId;
    private int targetId;
    private String targetType;
    private boolean like;

    public ReactionInputDto(int clientId, int targetId, String targetType, boolean like) {
        this.clientId = clientId;
        this.targetId = targetId;
        this.targetType = targetType;
        this.like = like;
    }

    protected ReactionInputDto() {
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
