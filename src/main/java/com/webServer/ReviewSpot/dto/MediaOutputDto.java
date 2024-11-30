package com.webServer.ReviewSpot.dto;

import java.util.List;

public class MediaOutputDto extends MediaInputDto{
    private int id;
    private float rating;
    private List<CommentOutputDto> comments;
    private List<ReviewOutputDto> reviews;

    public MediaOutputDto(String name, String photoUrl, String description, List<String> genre, int id, float rating, List<CommentOutputDto> comments, List<ReviewOutputDto> reviews) {
        super(name, photoUrl, description, genre);
        this.id = id;
        this.rating = rating;
        this.comments = comments;
        this.reviews = reviews;
    }

    public MediaOutputDto(int id, float rating, List<CommentOutputDto> comments, List<ReviewOutputDto> reviews) {
        this.id = id;
        this.rating = rating;
        this.comments = comments;
        this.reviews = reviews;
    }

    protected MediaOutputDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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
