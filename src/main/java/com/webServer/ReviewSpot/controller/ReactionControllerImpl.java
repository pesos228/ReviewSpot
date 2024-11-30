package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.ReactionController;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.reaction.ReactionFormModel;
import com.webServer.ReviewSpot.dto.ReactionInputDto;
import com.webServer.ReviewSpot.service.ReactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reaction")
public class ReactionControllerImpl implements ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionControllerImpl(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void createReaction(@Valid @ModelAttribute("reaction") ReactionFormModel reactionFormModel, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()){
            if(reactionFormModel.targetType().equalsIgnoreCase("COMMENT") || reactionFormModel.targetType().equalsIgnoreCase("REVIEW")){
                reactionService.save(new ReactionInputDto(reactionFormModel.clientId(),
                        reactionFormModel.targetId(), reactionFormModel.targetType(), reactionFormModel.like()));
            }
        }
    }

    @Override
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteReaction(@Valid @ModelAttribute("reaction") ReactionFormModel reactionFormModel, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()){
            if(reactionFormModel.targetType().equalsIgnoreCase("COMMENT")){
                reactionService.deleteByClientTargetAndType(reactionFormModel.clientId(), reactionFormModel.targetId(), "COMMENT");
            }
            if (reactionFormModel.targetType().equalsIgnoreCase("REVIEW")){
                reactionService.deleteByClientTargetAndType(reactionFormModel.clientId(), reactionFormModel.targetId(), "REVIEW");
            }
        }
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, clientName, clientPhotoUrl);
    }
}
