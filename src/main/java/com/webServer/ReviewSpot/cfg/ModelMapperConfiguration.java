package com.webServer.ReviewSpot.cfg;

import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.CommentOutputDto;
import com.webServer.ReviewSpot.dto.MediaCardDto;
import com.webServer.ReviewSpot.dto.ReviewOutputDto;
import com.webServer.ReviewSpot.entity.*;
import com.webServer.ReviewSpot.repository.CommentRepository;
import com.webServer.ReviewSpot.repository.ReactionRepository;
import com.webServer.ReviewSpot.repository.ReviewRepository;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.ReviewService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfiguration {

    private final ReactionRepository reactionRepository;
    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ModelMapperConfiguration(ReactionRepository reactionRepository, CommentRepository commentRepository, ReviewRepository reviewRepository) {
        this.reactionRepository = reactionRepository;
        this.commentRepository = commentRepository;
        this.reviewRepository = reviewRepository;
    }


    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        Converter<Review, ReviewOutputDto> reviewToReviewOutputDto = new Converter<Review, ReviewOutputDto>() {
            @Override
            public ReviewOutputDto convert(MappingContext<Review, ReviewOutputDto> mappingContext) {
                Review review = mappingContext.getSource();
                List<Reaction> reactions = reactionRepository.findByReviewId(review.getId());
                int likeCount = 0;
                int dislikeCount = 0;
                for (Reaction reaction: reactions){
                    if (reaction.getLike()){
                        likeCount++;
                    }
                    else{
                        dislikeCount++;
                    }

                }

                return new ReviewOutputDto(review.getClient().getId(), review.getMedia().getId(), review.getRating(),
                        review.getWatchStatus().toUpperCase(), review.getText(), review.getClient().getName(), review.getClient().getPhotoUrl(), review.getMedia().getName(), review.getMedia().getPhotoUrl(),
                        review.getId(), review.getDateTime(), likeCount, dislikeCount);

            }
        };

        Converter<Media, MediaCardDto> mediaToMediaCardDto = new Converter<Media, MediaCardDto>() {
            @Override
            public MediaCardDto convert(MappingContext<Media, MediaCardDto> mappingContext) {
                Media media = mappingContext.getSource();
                return new MediaCardDto(media.getId(), media.getName(), media.getPhotoUrl(),
                        media.getDescription(), media.getGenreList().stream().map(Genre::getName).collect(Collectors.toList()), media.getRating());
            }
        };

        Converter<Comment, CommentOutputDto> commentToCommentOutputDto = new Converter<Comment, CommentOutputDto>() {
            @Override
            public CommentOutputDto convert(MappingContext<Comment, CommentOutputDto> mappingContext) {
                Comment comment = mappingContext.getSource();
                List<Reaction> reactions = reactionRepository.findByCommentId(comment.getId());
                int likeCount = 0;
                int dislikeCount = 0;
                for (Reaction reaction: reactions){
                    if (reaction.getLike()){
                        likeCount++;
                    }
                    else{
                        dislikeCount++;
                    }
                }

                return new CommentOutputDto(comment.getClient().getId(), comment.getMedia().getId(), comment.getText(), comment.getId(),
                        comment.getClient().getName(), comment.getClient().getPhotoUrl(), comment.getMedia().getName(), comment.getDateTime(), likeCount, dislikeCount);

            }
        };

        Converter<Client, ClientInfoDto> clientToClientInfoDto = new Converter<Client, ClientInfoDto>() {
            @Override
            public ClientInfoDto convert(MappingContext<Client, ClientInfoDto> mappingContext) {
                Client client = mappingContext.getSource();

                List<CommentOutputDto> comments = commentRepository.findByClientId(client.getId()).stream()
                        .map(comment -> modelMapper.map(comment, CommentOutputDto.class)).toList();

                List<ReviewOutputDto> reviews = reviewRepository.findByClientId(client.getId()).stream().map(
                        review -> modelMapper.map(review, ReviewOutputDto.class)).toList();

                return new ClientInfoDto(client.getId(), client.getName(), client.getRole().getName().getRoleName(),client.getPhotoUrl(), comments, reviews);
            }
        };

        modelMapper.addConverter(reviewToReviewOutputDto);
        modelMapper.addConverter(mediaToMediaCardDto);
        modelMapper.addConverter(commentToCommentOutputDto);
        modelMapper.addConverter(clientToClientInfoDto);

        return modelMapper;
    }
}
