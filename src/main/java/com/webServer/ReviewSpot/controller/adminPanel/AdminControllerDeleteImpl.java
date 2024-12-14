package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminControllerDelete;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.webServer.ReviewSpot.exceptions.CommentNotFoundException;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.ReviewService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/delete")
public class AdminControllerDeleteImpl implements AdminControllerDelete {

    private final GenreService genreService;
    private final MediaService mediaService;
    private final CommentService commentService;
    private final ReviewService reviewService;

    @Autowired
    public AdminControllerDeleteImpl(GenreService genreService, MediaService mediaService, CommentService commentService, ReviewService reviewService) {
        this.genreService = genreService;
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.reviewService = reviewService;
    }

    @Override
    @PostMapping("/{id}/genre")
    public String deleteGenre(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            genreService.deleteById(id);
        }catch (GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Genre successfully deleted!");
        return "redirect:/admin/genre";
    }

    @Override
    @PostMapping("/{id}/media")
    public String deleteMedia(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            mediaService.deleteById(id);
        }catch (MediaNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Media successfully deleted!");
        return "redirect:/admin/media";
    }

    @Override
    @PostMapping("/{id}/comment")
    public String deleteComment(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteById(id);
        }catch (CommentNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Comment successfully deleted!");
        return "redirect:/admin/comment";
    }

    @Override
    @PostMapping("/{id}/review")
    public String deleteReview(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteById(id);
        }catch (CommentNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Review successfully deleted!");
        return "redirect:/admin/review";
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
