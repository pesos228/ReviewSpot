package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.MediaController;
import com.reviewSpot.models.viewmodel.MediaViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.CommentCardViewModel;
import com.reviewSpot.models.viewmodel.card.MediaCardViewModel;
import com.reviewSpot.models.viewmodel.card.ReviewCardViewModel;
import com.reviewSpot.models.viewmodel.form.comment.CommentPageFormModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewPageFormModel;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.ReactionService;
import com.webServer.ReviewSpot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String mediaPage(@ModelAttribute("commentFilter") CommentPageFormModel commentFilter,
                            @ModelAttribute("reviewFilter") ReviewPageFormModel reviewFilter,
                            @PathVariable int id, Model model){
        var commentPage = commentFilter.getPage() != null ? commentFilter.getPage() : 1;
        var commentSize = commentFilter.getSize() != null ? commentFilter.getSize() : 10;
        var reviewPage = reviewFilter.getPage() != null ? reviewFilter.getPage() : 1;
        var reviewSize = reviewFilter.getSize() != null ? reviewFilter.getSize() : 4;

        var base = createBaseViewModel("Media page", 2, null, null);
        var media = mediaService.findById(id);
        MediaCardViewModel mediaCard = new MediaCardViewModel(media.getId(), media.getName(), media.getPhotoUrl(), media.getDescription(), media.getGenre(), media.getRating());

        var comments = commentService.getLastCommentsByMediaId(id, commentPage, commentSize);
        var reviews = reviewService.getReviewsByMediaId(id, reviewPage, reviewSize);

        List<CommentCardViewModel> commentsCard = comments.stream().map(comment -> new CommentCardViewModel(comment.getId(), comment.getClientName(), comment.getClientPhotoUrl(), comment.getClientId(),
                comment.getMediaName(), comment.getText(), comment.getDateTime(), comment.getLikeCount(), comment.getDislikeCount(),
                reactionService.isLike(comment.getClientId(), comment.getId(), "COMMENT"), reactionService.isDislike(comment.getClientId(),
                comment.getId(), "COMMENT"), comments.getNumber() + 1, comments.getTotalPages())).toList();

        List<ReviewCardViewModel> reviewsCard = reviews.stream().map(review -> new ReviewCardViewModel(review.getId(), review.getClientName(), review.getClientPhotoUrl(), review.getMediaName(), review.getMediaId(),
                review.getMediaPhotoUrl(), review.getRating(), review.getWatchStatus().toString(), review.getText(), review.getDateTime(), review.getLikeCount(), review.getDislikeCount(),
                reactionService.isLike(review.getClientId(), review.getId(), "REVIEW"), reactionService.isDislike(review.getClientId(), review.getId(), "REVIEW"), reviews.getNumber() +1, reviews.getTotalPages())).toList();

        MediaViewModel mediaViewModel = new MediaViewModel(base, mediaCard, reviewsCard, commentsCard);
        model.addAttribute("model", mediaViewModel);

        return "media";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
