package com.webServer.ReviewSpot.dto;

public class ReviewInputDto {
    private int clientId;
    private int mediaId;
    private int rating;
    private String watchStatus;
    private String text;

    public ReviewInputDto(int clientId, int mediaId, int rating, String watchStatus, String text) {
        this.clientId = clientId;
        this.mediaId = mediaId;
        this.rating = rating;
        this.watchStatus = watchStatus;
        this.text = text;
    }

    protected ReviewInputDto() {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public enum WatchStatus {
        WATCHING,
        COMPLETED,
        DROPPED
    }
}
