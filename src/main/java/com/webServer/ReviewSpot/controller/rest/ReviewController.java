package com.webServer.ReviewSpot.controller.rest;

import com.webServer.ReviewSpot.dto.ReviewInputDto;
import com.webServer.ReviewSpot.dto.ReviewOutputDto;
import com.webServer.ReviewSpot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legacy/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    void saveReview(@RequestBody ReviewInputDto reviewInputDto){
        reviewService.save(reviewInputDto);
    }

    @GetMapping("/media/{id}")
    List<ReviewOutputDto> findByMediaId(@PathVariable int id){
        return reviewService.findByMediaId(id);
    }

    @GetMapping("/client/{id}")
    List<ReviewOutputDto> findByClientId(@PathVariable int id){
        return reviewService.findByClientId(id);
    }
}
