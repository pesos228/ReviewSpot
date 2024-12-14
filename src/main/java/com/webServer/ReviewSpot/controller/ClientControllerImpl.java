package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ClientController;
import com.reviewSpot.models.viewmodel.ClientProfileViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.ClientCardViewModel;
import com.reviewSpot.models.viewmodel.card.CommentCardViewModel;
import com.reviewSpot.models.viewmodel.card.ReviewCardViewModel;
import com.reviewSpot.models.viewmodel.form.comment.CommentPageFormModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewPageFormModel;
import com.webServer.ReviewSpot.dto.CommentOutputDto;
import com.webServer.ReviewSpot.dto.ReviewOutputDto;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.ReactionService;
import com.webServer.ReviewSpot.service.ReviewService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/client")
public class ClientControllerImpl implements ClientController {

    private final ClientService clientService;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final ReactionService reactionService;

    @Autowired
    public ClientControllerImpl(ClientService clientService, CommentService commentService, ReviewService reviewService, ReactionService reactionService) {
        this.clientService = clientService;
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.reactionService = reactionService;
    }

    @Override
    @GetMapping("/{id}")
    public String clientProfile(@ModelAttribute("commentForm") CommentPageFormModel commentForm,
                                @ModelAttribute("reviewForm") ReviewPageFormModel reviewForm,
                                @PathVariable int id,
                                Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var commentPage = commentForm.commentPage() != null ? commentForm.commentPage() : 1;
        var commentSize = commentForm.commentSize() != null ? commentForm.commentSize() : 3;
        var reviewPage = reviewForm.reviewPage() != null ? reviewForm.reviewPage() : 1;
        var reviewSize = reviewForm.reviewSize() != null ? reviewForm.reviewSize() : 3;

        commentForm = new CommentPageFormModel(commentPage, commentSize);
        reviewForm = new ReviewPageFormModel(reviewPage, reviewSize);

        var base = createBaseViewModel("Client profile", userDetails);
        var clientWeb = clientService.findById(id);

        Page<CommentOutputDto> lastComments = commentService.getLastCommentsByClientId(id, commentPage, commentSize);
        Page<ReviewOutputDto> lastReviews = reviewService.getReviewsByClientId(id, reviewPage, reviewSize);

        var clientCard = new ClientCardViewModel(clientWeb.getId(), clientWeb.getName(), clientWeb.getPhotoUrl(), clientWeb.getComments().size(), clientWeb.getReviews().size());
        var commentCard = lastComments.stream().map(comment -> new CommentCardViewModel(comment.getId(), comment.getClientName(), comment.getClientPhotoUrl(), comment.getClientId(),
                comment.getMediaName(), comment.getMediaId(), comment.getText(), comment.getDateTime(), comment.getLikeCount(), comment.getDislikeCount(), reactionService.isLike(base.clientId(), comment.getId(), "COMMENT"),
                reactionService.isDislike(base.clientId(), comment.getId(), "COMMENT"))).toList();
        var reviewCard = lastReviews.stream().map(review -> new ReviewCardViewModel(review.getId(), review.getClientName(), review.getClientPhotoUrl(), review.getClientId(),  review.getMediaName(), review.getMediaId(), review.getMediaPhotoUrl(),
                review.getRating(), review.getWatchStatus(), review.getText(), review.getDateTime(), review.getLikeCount(), review.getDislikeCount(), reactionService.isLike(base.clientId(), review.getId(), "REVIEW"),
                reactionService.isDislike(base.clientId(), review.getId(), "REVIEW"))).toList();


        var clientProfile = new ClientProfileViewModel(base, clientCard, commentCard, reviewCard, lastComments.getTotalPages(), lastReviews.getTotalPages());

        model.addAttribute("model", clientProfile);
        model.addAttribute("commentForm", commentForm);
        model.addAttribute("reviewForm", reviewForm);

        return "client-profile";
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
