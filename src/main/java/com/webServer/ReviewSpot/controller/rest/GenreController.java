package com.webServer.ReviewSpot.controller.rest;

import com.webServer.ReviewSpot.dto.GenreInputDto;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import com.webServer.ReviewSpot.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/legacy/genre")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @PostMapping
    void saveClient(@RequestBody GenreInputDto genreInputDto){
        genreService.save(genreInputDto);
    }

    @GetMapping
    List<GenreOutputDto> findAll(){
        return genreService.findAll();
    }
}
