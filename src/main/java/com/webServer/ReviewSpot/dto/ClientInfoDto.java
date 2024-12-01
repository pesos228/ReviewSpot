package com.webServer.ReviewSpot.dto;

import java.util.List;

public class ClientInfoDto {
    private int id;
    private String name;
    private String photoUrl;
    private List<CommentOutputDto> comments;
    private List<ReviewOutputDto> reviews;

    public ClientInfoDto(int id, String name, String photoUrl, List<CommentOutputDto> comments, List<ReviewOutputDto> reviews) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.comments = comments;
        this.reviews = reviews;
    }

    protected ClientInfoDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<CommentOutputDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentOutputDto> comments) {
        this.comments = comments;
    }

    public List<ReviewOutputDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewOutputDto> reviews) {
        this.reviews = reviews;
    }
}
