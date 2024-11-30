package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ClientController;
import com.reviewSpot.models.viewmodel.ClientProfileViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.ClientCardViewModel;
import com.reviewSpot.models.viewmodel.card.CommentCardViewModel;
import com.reviewSpot.models.viewmodel.card.ReviewCardViewModel;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.ReactionService;
import com.webServer.ReviewSpot.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String clientProfile(@PathVariable int id, Model model) {
        var base = createBaseViewModel("Client profile", null, null);
        var clientWeb = clientService.findById(id);
        var lastComments = commentService.getLastCommentsByClientId(id, 5);
        var lastReviews = reviewService.getLastReviewsByClientId(id, 9);

        var clientCard = new ClientCardViewModel(clientWeb.getName(), clientWeb.getPhotoUrl(), clientWeb.getComments().size(), clientWeb.getReviews().size());

        var commentCard = lastComments.stream().map(comment -> new CommentCardViewModel(comment.getClientName(), comment.getClientPhotoUrl(),
                comment.getMediaName(), comment.getText(), comment.getDateTime(), comment.getLikeCount(), comment.getDislikeCount(), reactionService.isLike(comment.getClientId(), comment.getId(), "COMMENT"),
                reactionService.isDislike(comment.getClientId(), comment.getId(), "COMMENT"))).toList();
        var reviewCard = lastReviews.stream().map(review -> new ReviewCardViewModel(review.getClientName(), review.getClientPhotoUrl(), review.getMediaName(), review.getMediaPhotoUrl(),
                review.getRating(), review.getWatchStatus().toString(), review.getText(), review.getDateTime(), review.getLikeCount(), review.getDislikeCount(), reactionService.isLike(review.getClientId(), review.getId(), "REVIEW"),
                reactionService.isDislike(review.getClientId(), review.getId(), "REVIEW"))).toList();


        var clientProfile = new ClientProfileViewModel(base, clientCard, commentCard, reviewCard);
        model.addAttribute("model", clientProfile);

        return "profile";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, clientName, clientPhotoUrl);
    }
}
