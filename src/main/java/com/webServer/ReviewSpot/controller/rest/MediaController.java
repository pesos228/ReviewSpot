package com.webServer.ReviewSpot.controller.rest;

import com.webServer.ReviewSpot.dto.MediaCardDto;
import com.webServer.ReviewSpot.dto.MediaInputDto;
import com.webServer.ReviewSpot.dto.MediaOutputDto;
import com.webServer.ReviewSpot.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legacy/media")
public class MediaController {
    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping
    void saveMedia(@RequestBody MediaInputDto mediaInputDto){
        mediaService.save(mediaInputDto);
    }

    @GetMapping
    List<MediaCardDto> findAll(){
        return mediaService.findAll();
    }

    @GetMapping("/search-name")
    List<MediaCardDto> findByNameContaining(@RequestParam String name){
        return mediaService.findByNameContaining(name);
    }

    @GetMapping("/search-genre")
    List<MediaCardDto> findByGenres(@RequestParam List<String> genres){
        return mediaService.findByGenres(genres);
    }

    @GetMapping("/{id}")
    MediaOutputDto findById(@PathVariable int id){
        return mediaService.findById(id);
    }
}
