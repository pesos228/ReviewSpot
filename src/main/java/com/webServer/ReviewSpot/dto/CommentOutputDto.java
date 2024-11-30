package com.webServer.ReviewSpot.dto;


import java.time.LocalDateTime;

public class CommentOutputDto extends CommentInputDto{
    private int id;
    private String clientName;
    private String clientPhotoUrl;
    private String mediaName;
    private LocalDateTime dateTime;
    private int likeCount;
    private int dislikeCount;

    public CommentOutputDto(int clientId, int mediaId, String text, int id, String clientName, String clientPhotoUrl, String mediaName, LocalDateTime dateTime, int likeCount, int dislikeCount) {
        super(clientId, mediaId, text);
        this.id = id;
        this.clientName = clientName;
        this.clientPhotoUrl = clientPhotoUrl;
        this.mediaName = mediaName;
        this.dateTime = dateTime;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public CommentOutputDto(int id, String clientName, String clientPhotoUrl, String mediaName, LocalDateTime dateTime, int likeCount, int dislikeCount) {
        this.id = id;
        this.clientName = clientName;
        this.clientPhotoUrl = clientPhotoUrl;
        this.mediaName = mediaName;
        this.dateTime = dateTime;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    protected CommentOutputDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
