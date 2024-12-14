package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminController;
import com.reviewSpot.models.viewmodel.AdminViewModelEntityList;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientPageFormModel;
import com.reviewSpot.models.viewmodel.form.comment.CommentPageFormModel;
import com.reviewSpot.models.viewmodel.form.genre.GenrePageFormModel;
import com.reviewSpot.models.viewmodel.form.media.MediaPageFormModel;
import com.reviewSpot.models.viewmodel.form.review.ReviewPageFormModel;
import com.webServer.ReviewSpot.service.*;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController {

    private final ClientService clientService;
    private final GenreService genreService;
    private final MediaService mediaService;
    private final CommentService commentService;
    private final ReviewService reviewService;

    @Autowired
    public AdminControllerImpl(ClientService clientService, GenreService genreService, MediaService mediaService, CommentService commentService, ReviewService reviewService) {
        this.clientService = clientService;
        this.genreService = genreService;
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @Override
    @GetMapping
    public String adminPanel(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("model", new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", userDetails),
                null, null, null, 0));
        return "admin-main";
    }

    @Override
    @GetMapping("/client")
    public String adminPanelClient(@ModelAttribute("clientForm") ClientPageFormModel clientForm, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var page = clientForm.clientPage() != null ? clientForm.clientPage() : 1;
        var size = clientForm.clientSize() != null ? clientForm.clientSize() : 10;
        clientForm = new ClientPageFormModel(page, size);
        var clients = clientService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", userDetails),
                clients.stream().toList(), clientForm, "client", clients.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    @GetMapping("/genre")
    public String adminPanelGenre(@ModelAttribute("genreForm") GenrePageFormModel genreForm, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var page = genreForm.genrePage() != null ? genreForm.genrePage() : 1;
        var size = genreForm.genreSize() != null ? genreForm.genreSize() : 5;
        genreForm = new GenrePageFormModel(page, size);
        var genres = genreService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", userDetails),
                genres.stream().toList(), genreForm, "genre", genres.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    @GetMapping("/media")
    public String adminPanelMedia(@ModelAttribute("mediaForm") MediaPageFormModel mediaForm, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var page = mediaForm.mediaPage() != null ? mediaForm.mediaPage() : 1;
        var size = mediaForm.mediaSize() != null ? mediaForm.mediaSize() : 10;
        mediaForm = new MediaPageFormModel(page, size);
        var medias = mediaService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", userDetails),
                medias.stream().toList(), mediaForm, "media", medias.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    @GetMapping("/comment")
    public String adminPanelComment(@ModelAttribute("mediaForm") CommentPageFormModel commentForm, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var page = commentForm.commentPage() != null ? commentForm.commentPage() : 1;
        var size = commentForm.commentSize() != null ? commentForm.commentSize() : 10;
        commentForm = new CommentPageFormModel(page, size);
        var comments = commentService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", userDetails), comments.stream().toList(),
                commentForm, "comment", comments.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    @GetMapping("/review")
    public String adminPanelReview(@ModelAttribute("reviewForm") ReviewPageFormModel reviewForm, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var page = reviewForm.reviewPage() != null ? reviewForm.reviewPage() : 1;
        var size = reviewForm.reviewSize() != null ? reviewForm.reviewSize() : 10;
        reviewForm = new ReviewPageFormModel(page, size);
        var reviews = reviewService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", userDetails), reviews.stream().toList(),
                reviewForm, "review", reviews.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
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
