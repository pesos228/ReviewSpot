package com.webServer.ReviewSpot.dto;

import java.util.List;

public class MediaInputDto {
    private String name;
    private String photoUrl;
    private String description;
    private List<String> genre;

    public MediaInputDto(String name, String photoUrl, String description, List<String> genre) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.description = description;
        this.genre = genre;
    }

    protected MediaInputDto() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }
}
