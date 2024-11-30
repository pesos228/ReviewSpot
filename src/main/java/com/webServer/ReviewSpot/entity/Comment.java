package com.webServer.ReviewSpot.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {
    private Client client;
    private Media media;
    private LocalDateTime dateTime;
    private String text;

    protected Comment() {
    }

    public Comment(Client client, Media media, LocalDateTime dateTime, String text) {
        this.client = client;
        this.media = media;
        this.dateTime = dateTime;
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

    @Column(name = "text", nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
