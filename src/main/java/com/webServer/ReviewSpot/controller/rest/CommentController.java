package com.webServer.ReviewSpot.controller.rest;

import com.webServer.ReviewSpot.dto.CommentInputDto;
import com.webServer.ReviewSpot.dto.CommentOutputDto;
import com.webServer.ReviewSpot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legacy/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    void saveComment(@RequestBody CommentInputDto commentInputDto){
        commentService.save(commentInputDto);
    }

    @GetMapping("/client/{id}")
    List<CommentOutputDto> findByClientId(@PathVariable int id){
        return commentService.findByClientId(id);
    }

    @GetMapping("/media/{id}")
    List<CommentOutputDto> findByMediaId(@PathVariable int id){
        return commentService.findByMediaId(id);
    }

    @DeleteMapping("/{id}")
    void deleteComment(@PathVariable int id){
        commentService.deleteById(id);
    }
}
