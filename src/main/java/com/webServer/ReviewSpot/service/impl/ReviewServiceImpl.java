package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.dto.ReviewInputDto;
import com.webServer.ReviewSpot.dto.ReviewOutputDto;
import com.webServer.ReviewSpot.entity.Client;
import com.webServer.ReviewSpot.entity.Media;
import com.webServer.ReviewSpot.entity.Reaction;
import com.webServer.ReviewSpot.entity.Review;
import com.webServer.ReviewSpot.exceptions.ClientAlreadyHaveReviewException;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.exceptions.ReviewNotFoundException;
import com.webServer.ReviewSpot.repository.ClientRepository;
import com.webServer.ReviewSpot.repository.MediaRepository;
import com.webServer.ReviewSpot.repository.ReactionRepository;
import com.webServer.ReviewSpot.repository.ReviewRepository;
import com.webServer.ReviewSpot.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClientRepository clientRepository;
    private final MediaRepository mediaRepository;
    private final ReactionRepository reactionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(ReviewRepository reviewRepository, ClientRepository clientRepository, MediaRepository mediaRepository, ReactionRepository reactionRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.clientRepository = clientRepository;
        this.mediaRepository = mediaRepository;
        this.reactionRepository = reactionRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public void save(ReviewInputDto reviewInputDto) {
        Client client = clientRepository.findById(reviewInputDto.getClientId());
        if (client == null){
            throw new ClientNotFoundException("Client with id:" + reviewInputDto.getClientId() + " not found");
        }

        Media media = mediaRepository.findById(reviewInputDto.getMediaId());
        if (media == null){
            throw new MediaNotFoundException("Media with id: " + reviewInputDto.getMediaId() + " not found");
        }

        List<Review> reviewsByMedia = media.getReviews();

        List<Integer> reviewRatingList = new ArrayList<>();
        for (Review review: reviewsByMedia){

            if (review.getClient().getId() == client.getId()){
                throw new ClientAlreadyHaveReviewException("Client with id: "+ client.getId() + " already have review on media with id: "+ media.getId());
            }

            reviewRatingList.add(review.getRating());
        }
        reviewRatingList.add(reviewInputDto.getRating());
        media.updateRating(reviewRatingList);

        Review review = new Review(client, media, LocalDateTime.now(), reviewInputDto.getRating(), reviewInputDto.getWatchStatus(), reviewInputDto.getText());
        reviewRepository.save(review);

        mediaRepository.save(media);
    }

    @Override
    public ReviewOutputDto findById(int id) {
        Review review = reviewRepository.findById(id);
        return modelMapper.map(review, ReviewOutputDto.class);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        Review review = reviewRepository.findById(id);
        if (review == null){
            throw new ReviewNotFoundException("Review with id: " + id + " not found");
        }

        Media media = review.getMedia();
        reviewRepository.deleteById(id);

        List<Review> reviews = reviewRepository.findByMediaId(media.getId());
        List<Integer> ratings = new ArrayList<>();

        for (Review rev : reviews) {
            ratings.add(rev.getRating());
        }

        media.updateRating(ratings);
        mediaRepository.save(media);
    }

    @Override
    public List<ReviewOutputDto> findByMediaId(int id) {
        Media media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with id: " + id + " not found");
        }
        List<Review> reviews = reviewRepository.findByMediaId(id);

        return reviews.stream().map(review -> modelMapper.map(review, ReviewOutputDto.class)).toList();
    }

    @Override
    public List<ReviewOutputDto> findByClientId(int id) {
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id:" + id + " not found");
        }
        List<Review> reviews = reviewRepository.findByClientId(id);

        return reviews.stream().map(review -> modelMapper.map(review, ReviewOutputDto.class)).toList();
    }

    @Override
    public List<ReviewOutputDto> findByClientIdAfterDate(int id, LocalDateTime date) {
        var reviews = reviewRepository.findByClientIdAfterDate(id, date);
        return reviews.stream().map(review -> modelMapper.map(review, ReviewOutputDto.class)).toList();
    }

    @Override
    public List<ReviewOutputDto> findByMediaIdAfterDate(int id, LocalDateTime date) {
        var reviews = reviewRepository.findByMediaIdAfterDate(id, date);
        return reviews.stream().map(review -> modelMapper.map(review, ReviewOutputDto.class)).toList();
    }

    @Override
    public List<ReviewOutputDto> getLastReviewsByClientId(int id, int count) {
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + id + " not found3");
        }
        var reviews = reviewRepository.getLastReviewsByClientId(id, count);
        return reviews.stream().map(review -> modelMapper.map(review, ReviewOutputDto.class)).toList();
    }

    @Override
    public Page<ReviewOutputDto> getReviewsByMediaId(int id, int reviewPage, int reviewSize) {
        Pageable pageable = PageRequest.of(reviewPage - 1, reviewSize);
        Media media = mediaRepository.findById(id);
        if (media == null){
            throw new MediaNotFoundException("Media with id: " + id + " not found");
        }

        var reviews = reviewRepository.getReviewsByMediaId(id, pageable);
        return reviews.map(review -> modelMapper.map(review, ReviewOutputDto.class));
    }

    @Override
    public Page<ReviewOutputDto> getReviewsByClientId(int id, int reviewPage, int reviewSize) {
        Pageable pageable = PageRequest.of(reviewPage - 1, reviewSize);
        Client client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + id + " not found");
        }

        var reviews = reviewRepository.getReviewsByClientId(id, pageable);
        return reviews.map(review -> modelMapper.map(review, ReviewOutputDto.class));
    }

    @Override
    public ReviewOutputDto findByClientIdMediaId(int clientId, int mediaId) {
        var review = reviewRepository.findByClientIdMediaId(clientId, mediaId);
        if (review == null){
            throw new ReviewNotFoundException("Client with id: " + clientId + " doesnt have a review with id: " + mediaId);
        }
        return modelMapper.map(review, ReviewOutputDto.class);
    }

}
