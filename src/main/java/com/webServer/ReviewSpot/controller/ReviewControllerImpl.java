package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ReviewController;
import com.reviewSpot.models.viewmodel.ReviewViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewFormModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewPageFormModel;
import com.webServer.ReviewSpot.dto.ReviewInputDto;
import com.webServer.ReviewSpot.exceptions.ClientAlreadyHaveReviewException;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.exceptions.ReviewNotFoundException;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.ReactionService;
import com.webServer.ReviewSpot.service.ReviewService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    public String reviewPreview(@PathVariable int id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var review = reviewService.findById(id);
        var media = mediaService.findById(review.getMediaId());
        var baseView = createBaseViewModel("Review page", userDetails);

        model.addAttribute("model", new ReviewViewModel(baseView, review.getId(), media.getName(), media.getPhotoUrl(), media.getId(), review.getClientName(), review.getClientId(), review.getWatchStatus().toString(),
                review.getText(), review.getRating(), review.getLikeCount(), review.getDislikeCount(), reactionService.isLike(baseView.clientId(), id, "REVIEW"),
                reactionService.isDislike(baseView.clientId(), id, "REVIEW")));

        return "review";
    }

    @Override
    @PostMapping
    public String createReview(@Valid @ModelAttribute("review")ReviewFormModel reviewFormModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            return "redirect:/";
        }

        try {
            reviewService.save(new ReviewInputDto(reviewFormModel.clientId(), reviewFormModel.mediaId(), reviewFormModel.rating(), reviewFormModel.watchStatus(), reviewFormModel.text()));
            return "redirect:" + reviewFormModel.currentUrl();
        }catch (ClientNotFoundException | MediaNotFoundException | ClientAlreadyHaveReviewException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + reviewFormModel.currentUrl();
        }
    }

    @Override
    @PostMapping("/delete")
    public String deleteReview(@RequestParam("reviewId") int reviewId, @RequestParam("currentUrl") String currentUrl, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteById(reviewId);
            return "redirect:" + currentUrl;
        }catch (ReviewNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + currentUrl;
        }
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, UserDetails userDetails) {
        if (userDetails == null){
            return new BaseViewModel(title, -1, null, null);
        }
        else{
            UserDetailsServiceImpl.CustomUser customUser = (UserDetailsServiceImpl.CustomUser) userDetails;
            return new BaseViewModel(title, customUser.getId(), customUser.getName(), customUser.getPhotoUrl());
        }
    }
}
