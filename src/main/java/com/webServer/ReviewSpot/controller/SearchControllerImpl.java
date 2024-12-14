package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.SearchController;
import com.reviewSpot.models.viewmodel.SearchViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.MediaCardViewModel;
import com.reviewSpot.models.viewmodel.form.media.MediaSearchFormModel;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/search")
public class SearchControllerImpl implements SearchController {

    private final MediaService mediaService;
    private final GenreService genreService;

    @Autowired
    public SearchControllerImpl(MediaService mediaService, GenreService genreService) {
        this.mediaService = mediaService;
        this.genreService = genreService;
    }

    @Override
    @GetMapping
    public String search(@ModelAttribute("filter") MediaSearchFormModel filter, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var searchQuery = filter.searchQuery() != null ? filter.searchQuery() : "";
        var genres = filter.genres() != null ? filter.genres() : new ArrayList<String>();
        var page = filter.page() != null ? filter.page() : 1;
        var size = filter.size() != null ? filter.size() : 5;
        var base = createBaseViewModel("Search", userDetails);

        filter = new MediaSearchFormModel(searchQuery, genres, page, size);

        var mediaPage = mediaService.getMedia(searchQuery, genres, page, size);
        var mediaViewModel = mediaPage.stream()
                .map(mediaCardDto -> new MediaCardViewModel(mediaCardDto.getId(), mediaCardDto.getName(), mediaCardDto.getMediaPhotoUrl(),
                        mediaCardDto.getDescription(), mediaCardDto.getGenres(), mediaCardDto.getRating()))
                .collect(Collectors.toList());

        var viewModel = new SearchViewModel(base, filter, mediaViewModel, mediaPage.getTotalPages());

        model.addAttribute("model", viewModel);
        model.addAttribute("genres", genreService.findAll().stream().map(GenreOutputDto::getName).toList());

        return "search";
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
