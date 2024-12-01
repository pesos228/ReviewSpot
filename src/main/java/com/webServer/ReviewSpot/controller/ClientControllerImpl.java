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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public String clientProfile(@RequestParam(name = "commentFilter.page", required = false, defaultValue = "1") Integer commentPage,
                                @RequestParam(name = "commentFilter.size", required = false, defaultValue = "3") Integer commentSize,
                                @RequestParam(name = "reviewFilter.page", required = false, defaultValue = "1") Integer reviewPage,
                                @RequestParam(name = "reviewFilter.size", required = false, defaultValue = "4") Integer reviewSize,
                                @PathVariable int id,
                                Model model) {


        var commentFilter = new CommentPageFormModel(commentPage, commentSize);
        var reviewFilter = new ReviewPageFormModel(reviewPage, reviewSize);

        System.out.println("Final commentPage: " + commentPage);
        System.out.println("Final commentSize: " + commentSize);

        var base = createBaseViewModel("Client profile", 2,"Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");
        var clientWeb = clientService.findById(id);

        Page<CommentOutputDto> lastComments = commentService.getLastCommentsByClientId(id, commentPage, commentSize);
        Page<ReviewOutputDto> lastReviews = reviewService.getReviewsByClientId(id, reviewPage, reviewSize);

        var clientCard = new ClientCardViewModel(clientWeb.getId(), clientWeb.getName(), clientWeb.getPhotoUrl(), clientWeb.getComments().size(), clientWeb.getReviews().size());
        var commentCard = lastComments.stream().map(comment -> new CommentCardViewModel(comment.getId(), comment.getClientName(), comment.getClientPhotoUrl(), comment.getClientId(),
                comment.getMediaName(), comment.getText(), comment.getDateTime(), comment.getLikeCount(), comment.getDislikeCount(), reactionService.isLike(base.clientId(), comment.getId(), "COMMENT"),
                reactionService.isDislike(base.clientId(), comment.getId(), "COMMENT"), lastComments.getNumber() + 1, lastComments.getTotalPages())).toList();
        var reviewCard = lastReviews.stream().map(review -> new ReviewCardViewModel(review.getId(), review.getClientName(), review.getClientPhotoUrl(), review.getMediaName(), review.getMediaId(), review.getMediaPhotoUrl(),
                review.getRating(), review.getWatchStatus().toString(), review.getText(), review.getDateTime(), review.getLikeCount(), review.getDislikeCount(), reactionService.isLike(base.clientId(), review.getId(), "REVIEW"),
                reactionService.isDislike(base.clientId(), review.getId(), "REVIEW"), lastReviews.getNumber() + 1, lastReviews.getTotalPages())).toList();


        var clientProfile = new ClientProfileViewModel(base, clientCard, commentCard, reviewCard);

        model.addAttribute("model", clientProfile);
        model.addAttribute("commentFilter", commentFilter);
        model.addAttribute("reviewFilter", reviewFilter);

        return "client-profile";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
