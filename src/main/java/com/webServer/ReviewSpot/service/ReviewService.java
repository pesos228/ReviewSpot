package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.ReviewInputDto;
import com.webServer.ReviewSpot.dto.ReviewOutputDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {
    void save(ReviewInputDto reviewInputDto);
    ReviewOutputDto findById(int id);
    void deleteById(int reviewId);
    List<ReviewOutputDto> findByMediaId(int id);
    List<ReviewOutputDto> findByClientId(int id);
    List<ReviewOutputDto> findByClientIdAfterDate(int id, LocalDateTime date);
    List<ReviewOutputDto> findByMediaIdAfterDate(int id, LocalDateTime date);
    List<ReviewOutputDto> getLastReviewsByClientId(int id, int count);
    Page<ReviewOutputDto> getReviewsByMediaId(int id, int reviewPage, int reviewSize);
    Page<ReviewOutputDto> getReviewsByClientId(int id, int reviewPage, int reviewSize);
    ReviewOutputDto findByClientIdMediaId(int clientId, int mediaId);
    Page<ReviewOutputDto> findAll(int page, int size);
    boolean hasDeletePermission(int reviewId, int clientId);
}
