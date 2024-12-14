package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.CommentController;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.comment.CommentFormModel;
import com.webServer.ReviewSpot.dto.CommentInputDto;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.exceptions.CommentNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/comment")
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentControllerImpl(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    @PostMapping
    public String createComment(@Valid @ModelAttribute("commentForm")CommentFormModel commentFormModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            return "redirect:/";
        }

        try {
            commentService.save(new CommentInputDto(commentFormModel.clientId(), commentFormModel.mediaId(), commentFormModel.text()));
            return "redirect:" + commentFormModel.currentUrl();
        }catch (ClientNotFoundException | MediaNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:" + commentFormModel.currentUrl();
        }

    }

    @Override
    @PostMapping("/delete")
    public String deleteComment(@RequestParam("commentId") int commentId, @RequestParam("currentUrl") String currentUrl, RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteById(commentId);
            return "redirect:" + currentUrl;
        }catch (CommentNotFoundException e){
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
