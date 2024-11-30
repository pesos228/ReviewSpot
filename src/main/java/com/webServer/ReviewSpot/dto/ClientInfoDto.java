package com.webServer.ReviewSpot.dto;

import java.util.List;

public class ClientInfoDto {
    private String name;
    private String photoUrl;
    private List<CommentOutputDto> comments;
    private List<ReviewOutputDto> reviews;

    public ClientInfoDto(String name, String photoUrl, List<CommentOutputDto> comments, List<ReviewOutputDto> reviews) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.comments = comments;
        this.reviews = reviews;
    }

    protected ClientInfoDto() {
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
