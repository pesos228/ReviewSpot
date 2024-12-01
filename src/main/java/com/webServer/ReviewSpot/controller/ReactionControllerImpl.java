package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ReactionController;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.reaction.ReactionFormModel;
import com.webServer.ReviewSpot.dto.ReactionInputDto;
import com.webServer.ReviewSpot.exceptions.*;
import com.webServer.ReviewSpot.service.ReactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reaction")
public class ReactionControllerImpl implements ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionControllerImpl(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @Override
    @PostMapping
    public String createReaction(@Valid @ModelAttribute("reaction") ReactionFormModel reactionFormModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()){
            try {
                if(reactionFormModel.targetType().equalsIgnoreCase("COMMENT") || reactionFormModel.targetType().equalsIgnoreCase("REVIEW")){
                    reactionService.save(new ReactionInputDto(reactionFormModel.clientId(),
                            reactionFormModel.targetId(), reactionFormModel.targetType(), reactionFormModel.like()));
                    return "redirect:" + reactionFormModel.currentUrl();
                }
            }catch (ClientNotFoundException | ReviewNotFoundException | ClientAlreadyHaveReactionException | CommentNotFoundException | UnknownTargetTypeException e){
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:" + reactionFormModel.currentUrl();
            }
        }
        redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
        return "redirect:" + reactionFormModel.currentUrl();
    }

    @Override
    @PostMapping("/delete")
    public String deleteReaction(@Valid @ModelAttribute("reaction") ReactionFormModel reactionFormModel, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasErrors()){
            try {

                if(reactionFormModel.targetType().equalsIgnoreCase("COMMENT")){
                    reactionService.deleteByClientTargetAndType(reactionFormModel.clientId(), reactionFormModel.targetId(), "COMMENT");
                }
                if (reactionFormModel.targetType().equalsIgnoreCase("REVIEW")){
                    reactionService.deleteByClientTargetAndType(reactionFormModel.clientId(), reactionFormModel.targetId(), "REVIEW");
                }

                return "redirect:" + reactionFormModel.currentUrl();

            }catch (ClientNotFoundException | ClientReactionNotFoundException e){
                redirectAttributes.addFlashAttribute("error", e.getMessage());
                return "redirect:" + reactionFormModel.currentUrl();
            }

        }
        redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
        return "redirect:" + reactionFormModel.currentUrl();
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
