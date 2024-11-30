package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.ReviewInputDto;
import com.webServer.ReviewSpot.dto.ReviewOutputDto;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface ReviewService {
    void save(ReviewInputDto reviewInputDto);
    ReviewOutputDto findById(int id);
    void deleteById(int id);
    List<ReviewOutputDto> findByMediaId(int id);
    List<ReviewOutputDto> findByClientId(int id);
    List<ReviewOutputDto> findByClientIdAfterDate(int id, LocalDateTime date);
    List<ReviewOutputDto> findByMediaIdAfterDate(int id, LocalDateTime date);
    List<ReviewOutputDto> getLastReviewsByClientId(int id, int count);
    Page<ReviewOutputDto>  getLastReviewsByMediaId(int id, int reviewPage, int reviewSize);
}