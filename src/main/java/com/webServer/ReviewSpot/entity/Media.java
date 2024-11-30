package com.webServer.ReviewSpot.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "media")
public class Media extends BaseEntity{
    private String name;
    private String photoUrl;
    private String description;
    private float rating;
    private List<Comment> comments;
    private List<Review> reviews;
    private List<Genre> genreList;

    protected Media() {
    }

    public Media(String name, String photoUrl, String description, float rating, List<Comment> comments, List<Review> reviews, List<Genre> genreList) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.description = description;
        this.rating = rating;
        this.comments = comments;
        this.reviews = reviews;
        this.genreList = genreList;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "photo_url", nullable = false)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Column(name = "description", nullable = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "rating")
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @OneToMany(mappedBy = "media")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToMany(mappedBy = "media")
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @ManyToMany
    @JoinTable(
            name = "media_genre",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    public List<Genre> getGenreList() {
        return genreList;
    }

    public void setGenreList(List<Genre> genreList) {
        this.genreList = genreList;
    }

    public void updateRating(List<Integer> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            this.rating = 0;
            return;
        }

        int sum = 0;
        for (int rating : ratings) {
            sum += rating;
        }
        this.rating = (float) sum / ratings.size();
    }

}
