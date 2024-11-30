package com.webServer.ReviewSpot.dto;

import java.time.LocalDateTime;

public class ReviewOutputDto extends ReviewInputDto{
    private String clientName;
    private String clientPhotoUrl;
    private String mediaName;
    private String mediaPhotoUrl;
    private int id;
    private LocalDateTime dateTime;
    private int likeCount;
    private int dislikeCount;

    public ReviewOutputDto(int clientId, int mediaId, int rating, WatchStatus watchStatus, String text, String clientName, String clientPhotoUrl, String mediaName, String mediaPhotoUrl, int id, LocalDateTime dateTime, int likeCount, int dislikeCount) {
        super(clientId, mediaId, rating, watchStatus, text);
        this.clientName = clientName;
        this.clientPhotoUrl = clientPhotoUrl;
        this.mediaName = mediaName;
        this.mediaPhotoUrl = mediaPhotoUrl;
        this.id = id;
        this.dateTime = dateTime;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public ReviewOutputDto(String clientName, String clientPhotoUrl, String mediaName, String mediaPhotoUrl, int id, LocalDateTime dateTime, int likeCount, int dislikeCount) {
        this.clientName = clientName;
        this.clientPhotoUrl = clientPhotoUrl;
        this.mediaName = mediaName;
        this.mediaPhotoUrl = mediaPhotoUrl;
        this.id = id;
        this.dateTime = dateTime;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    protected ReviewOutputDto() {
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhotoUrl() {
        return clientPhotoUrl;
    }

    public void setClientPhotoUrl(String clientPhotoUrl) {
        this.clientPhotoUrl = clientPhotoUrl;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaPhotoUrl() {
        return mediaPhotoUrl;
    }

    public void setMediaPhotoUrl(String mediaPhotoUrl) {
        this.mediaPhotoUrl = mediaPhotoUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public void setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
    }
}
