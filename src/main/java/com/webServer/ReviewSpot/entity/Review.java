package com.webServer.ReviewSpot.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "review")
public class Review extends BaseEntity {
    private Client client;
    private Media media;
    private LocalDateTime dateTime;
    private int rating;
    private String watchStatus;
    private String text;
    protected Review() {
    }

    public Review(Client client, Media media, LocalDateTime dateTime, int rating, String watchStatus, String text) {
        this.client = client;
        this.media = media;
        this.dateTime = dateTime;
        this.rating = rating;
        this.watchStatus = watchStatus;
        this.text = text;
    }

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @ManyToOne
    @JoinColumn(name = "media_id", nullable = false)
    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Column(name = "date", nullable = false)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Column(name = "rating", nullable = false)
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Column(name = "text", columnDefinition = "TEXT")
    public String getText() {
        return text;
    }

    @Column(name = "watch_status", nullable = false)
    public String getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(String watchStatus) {
        this.watchStatus = watchStatus;
    }

    public void setText(String text) {
        this.text = text;
    }

}
