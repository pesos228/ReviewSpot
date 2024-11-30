package com.webServer.ReviewSpot.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "client")
public class Client extends BaseEntity{
    private String name;
    private String email;
    private String password;
    private String photoUrl;
    private List<Comment> comments;
    private List<Review> reviews;
    private List<Reaction> reactions;

    protected Client(){}

    public Client(String name, String email, String password, String photoUrl, List<Comment> comments, List<Review> reviews, List<Reaction> reactions) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.photoUrl = photoUrl;
        this.comments = comments;
        this.reviews = reviews;
        this.reactions = reactions;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email", nullable = false, unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    @Column(name = "photo_url", nullable = false)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "client")
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @OneToMany(mappedBy = "client")
    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @OneToMany(mappedBy = "client")
    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }
}
