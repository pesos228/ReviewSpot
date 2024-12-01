package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ReviewController;
import com.reviewSpot.models.viewmodel.ReviewViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewPageFormModel;
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


@Controller
@RequestMapping("/review")
public class ReviewControllerImpl implements ReviewController {

    private final ReviewService reviewService;
    private final ReactionService reactionService;
    private final MediaService mediaService;

    @Autowired
    public ReviewControllerImpl(ReviewService reviewService, ReactionService reactionService, MediaService mediaService) {
        this.reviewService = reviewService;
        this.reactionService = reactionService;
        this.mediaService = mediaService;
    }

    @Override
    @GetMapping("/{id}")
    public String reviewPreview(@PathVariable int id, Model model) {
        var review = reviewService.findById(id);
        var media = mediaService.findById(review.getMediaId());
        var baseView = createBaseViewModel("Review page", 2, null, null);

        model.addAttribute("model", new ReviewViewModel(baseView, media.getName(), review.getClientName(), review.getWatchStatus().toString(),
                review.getText(), review.getRating(), review.getLikeCount(), review.getDislikeCount(), reactionService.isLike(review.getClientId(), id, "REVIEW"),
                reactionService.isDislike(review.getClientId(), id, "REVIEW")));

        return "review";
    }

    @Override
    @GetMapping("/client/{id}")
    public String reviewsClient(@ModelAttribute("filter") ReviewPageFormModel reviewPageFormModel, @PathVariable int id, Model model) {
        return "";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
