package com.webServer.ReviewSpot.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "genre")
public class Genre extends BaseEntity{
    private String name;
    private List<Media> mediaList;

    protected Genre(){}

    public Genre(String name, List<Media> mediaList) {
        this.name = name;
        this.mediaList = mediaList;
    }

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "genreList")
    public List<Media> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<Media> mediaList) {
        this.mediaList = mediaList;
    }
}
