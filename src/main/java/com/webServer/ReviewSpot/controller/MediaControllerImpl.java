package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.MediaController;
import com.reviewSpot.models.viewmodel.MediaViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.CommentCardViewModel;
import com.reviewSpot.models.viewmodel.card.MediaCardViewModel;
import com.reviewSpot.models.viewmodel.card.ReviewCardViewModel;
import com.reviewSpot.models.viewmodel.form.comment.CommentFormModel;
import com.reviewSpot.models.viewmodel.form.comment.CommentPageFormModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewFormModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewPageFormModel;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.ReactionService;
import com.webServer.ReviewSpot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/media")
public class MediaControllerImpl implements MediaController {

    private final MediaService mediaService;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final ReactionService reactionService;

    @Autowired
    public MediaControllerImpl(MediaService mediaService, CommentService commentService, ReviewService reviewService, ReactionService reactionService) {
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.reactionService = reactionService;
    }

    @Override
    @GetMapping("/{id}")
    public String mediaPage(@RequestParam(name = "commentFilter.page", required = false, defaultValue = "1") Integer commentPage,
                            @RequestParam(name = "commentFilter.size", required = false, defaultValue = "3") Integer commentSize,
                            @RequestParam(name = "reviewFilter.page", required = false, defaultValue = "1") Integer reviewPage,
                            @RequestParam(name = "reviewFilter.size", required = false, defaultValue = "4") Integer reviewSize,
                            @PathVariable int id, Model model){

        var commentFilter = new CommentPageFormModel(commentPage, commentSize);
        var reviewFilter = new ReviewPageFormModel(reviewPage, reviewSize);
        var base = createBaseViewModel("Media page", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");
        var media = mediaService.findById(id);
        MediaCardViewModel mediaCard = new MediaCardViewModel(media.getId(), media.getName(), media.getPhotoUrl(), media.getDescription(), media.getGenre(), media.getRating());

        var comments = commentService.getLastCommentsByMediaId(id, commentPage, commentSize);
        var reviews = reviewService.getReviewsByMediaId(id, reviewPage, reviewSize);

        List<CommentCardViewModel> commentsCard = comments.stream().map(comment -> new CommentCardViewModel(comment.getId(), comment.getClientName(), comment.getClientPhotoUrl(), comment.getClientId(),
                comment.getMediaName(), comment.getMediaId(), comment.getText(), comment.getDateTime(), comment.getLikeCount(), comment.getDislikeCount(),
                reactionService.isLike(base.clientId(), comment.getId(), "COMMENT"), reactionService.isDislike(base.clientId(),
                comment.getId(), "COMMENT"), comments.getNumber() + 1, comments.getTotalPages())).toList();

        List<ReviewCardViewModel> reviewsCard = reviews.stream().map(review -> new ReviewCardViewModel(review.getId(), review.getClientName(), review.getClientPhotoUrl(), review.getClientId(), review.getMediaName(), review.getMediaId(),
                review.getMediaPhotoUrl(), review.getRating(), review.getWatchStatus().toString(), review.getText(), review.getDateTime(), review.getLikeCount(), review.getDislikeCount(),
                reactionService.isLike(base.clientId(), review.getId(), "REVIEW"), reactionService.isDislike(base.clientId(), review.getId(), "REVIEW"), reviews.getNumber() +1, reviews.getTotalPages())).toList();

        MediaViewModel mediaViewModel = new MediaViewModel(base, mediaCard, reviewsCard, commentsCard);

        model.addAttribute("model", mediaViewModel);
        model.addAttribute("commentFilter", commentFilter);
        model.addAttribute("reviewFilter", reviewFilter);
        return "media";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
