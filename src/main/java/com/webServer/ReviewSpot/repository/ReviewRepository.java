package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewRepository {
    void save(Review review);
    List<Review> findByMediaId(int id);
    List<Review> findByClientId(int id);
    List<Review> findByClientIdAfterDate(int id, LocalDateTime date);
    List<Review> findByMediaIdAfterDate(int id, LocalDateTime date);
    List<Review> getLastReviewsByClientId(int id, int count);
    void deleteById(int id);
    Review findById(int id);
    Page<Review> getReviewsByMediaId(int id, Pageable pageable);
    Page<Review> getReviewsByClientId(int id, Pageable pageable);
    Review findByClientIdMediaId(int clientId, int mediaId);
}
