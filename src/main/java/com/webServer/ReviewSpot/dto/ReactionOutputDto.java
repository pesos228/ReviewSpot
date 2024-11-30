package com.webServer.ReviewSpot.dto;

public class ReactionOutputDto extends ReactionInputDto{
    private int id;

    public ReactionOutputDto(int clientId, int targetId, String targetType, boolean isLike, int id) {
        super(clientId, targetId, targetType, isLike);
        this.id = id;
    }

    public ReactionOutputDto(int id) {
        this.id = id;
    }

    protected ReactionOutputDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
