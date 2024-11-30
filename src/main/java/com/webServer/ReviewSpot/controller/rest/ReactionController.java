package com.webServer.ReviewSpot.controller.rest;

import com.webServer.ReviewSpot.dto.ReactionInputDto;
import com.webServer.ReviewSpot.service.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/legacy/reaction")
public class ReactionController {
    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping
    void saveReaction(@RequestBody ReactionInputDto reactionInputDto){
        reactionService.save(reactionInputDto);
    }

    @DeleteMapping("/{id}")
    void deleteReactionById(@PathVariable int id){
        reactionService.deleteById(id);
    }
}
