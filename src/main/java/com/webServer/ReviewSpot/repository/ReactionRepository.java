package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Reaction;

import java.util.List;

public interface ReactionRepository {
    void save(Reaction reaction);
    List<Reaction> findByCommentId(int id);
    List<Reaction> findByReviewId(int id);
    void deleteById(int id);
    Reaction findById(int id);
}
