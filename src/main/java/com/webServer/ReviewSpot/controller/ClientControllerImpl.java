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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String clientProfile(@ModelAttribute("reviewFilter") ReviewPageFormModel reviewFilter, @ModelAttribute("commentFilter") CommentPageFormModel commentFilter, @PathVariable int id, Model model) {
        var commentPage = commentFilter.page() != null ? commentFilter.page() : 1;
        var commentSize = commentFilter.size() != null ? commentFilter.size() : 3;
        var reviewPage = reviewFilter.page() != null ? reviewFilter.page() : 1;
        var reviewSize = reviewFilter.size() != null ? reviewFilter.size() : 4;

        var safeCommentFilter = new CommentPageFormModel(commentPage, commentSize);
        var safeReviewFilter = new ReviewPageFormModel(reviewPage, reviewSize);

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
        model.addAttribute("commentFilter", safeCommentFilter);
        model.addAttribute("reviewFilter", safeReviewFilter);

        return "client-profile";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
