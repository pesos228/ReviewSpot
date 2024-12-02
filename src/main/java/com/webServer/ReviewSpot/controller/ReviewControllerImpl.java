package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ReviewController;
import com.reviewSpot.models.viewmodel.ReviewViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewFormModel;
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
        var baseView = createBaseViewModel("Review page", 2,"Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");

        model.addAttribute("model", new ReviewViewModel(baseView, review.getId(), media.getName(), media.getPhotoUrl(), media.getId(), review.getClientName(), review.getClientId(), review.getWatchStatus().toString(),
                review.getText(), review.getRating(), review.getLikeCount(), review.getDislikeCount(), reactionService.isLike(baseView.clientId(), id, "REVIEW"),
                reactionService.isDislike(baseView.clientId(), id, "REVIEW")));

        model.addAttribute("reviewForm", new ReviewFormModel(baseView.clientId(), media.getId(), -1, null, null));

        return "review";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
