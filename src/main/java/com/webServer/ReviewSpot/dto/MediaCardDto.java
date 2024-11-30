package com.webServer.ReviewSpot.dto;

import java.util.List;

public class MediaCardDto {
    private int id;
    private String name;
    private String mediaPhotoUrl;
    private String description;
    List<String> genres;
    float rating;

    public MediaCardDto(int id, String name, String mediaPhotoUrl, String description, List<String> genres, float rating) {
        this.id = id;
        this.name = name;
        this.mediaPhotoUrl = mediaPhotoUrl;
        this.description = description;
        this.genres = genres;
        this.rating = rating;
    }

    protected MediaCardDto() {
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

    public String getMediaPhotoUrl() {
        return mediaPhotoUrl;
    }

    public void setMediaPhotoUrl(String mediaPhotoUrl) {
        this.mediaPhotoUrl = mediaPhotoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
