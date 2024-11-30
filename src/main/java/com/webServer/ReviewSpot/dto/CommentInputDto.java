package com.webServer.ReviewSpot.dto;

public class CommentInputDto {
    private int clientId;
    private int mediaId;
    private String text;

    public CommentInputDto(int clientId, int mediaId, String text) {
        this.clientId = clientId;
        this.mediaId = mediaId;
        this.text = text;
    }

    protected CommentInputDto() {
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
