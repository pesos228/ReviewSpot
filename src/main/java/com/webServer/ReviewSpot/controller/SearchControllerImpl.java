package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.SearchController;
import com.reviewSpot.models.viewmodel.SearchViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.MediaCardViewModel;
import com.reviewSpot.models.viewmodel.form.media.MediaSearchFormModel;
import com.webServer.ReviewSpot.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public SearchControllerImpl(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    @GetMapping
    public String search(@ModelAttribute("filter") MediaSearchFormModel filter, Model model) {
        var searchQuery = filter.searchQuery() != null ? filter.searchQuery() : "";
        var genres = filter.genres() != null ? filter.genres() : new ArrayList<String>();
        var page = filter.page() != null ? filter.page() : 1;
        var size = filter.size() != null ? filter.size() : 10;
        var base = createBaseViewModel("Search", 2, null, null);

        filter = new MediaSearchFormModel(searchQuery, genres, page, size);

        var mediaPage = mediaService.getMedia(searchQuery, genres, page, size);
        var mediaViewModel = mediaPage.stream()
                .map(mediaCardDto -> new MediaCardViewModel(mediaCardDto.getId(), mediaCardDto.getName(), mediaCardDto.getMediaPhotoUrl(),
                        mediaCardDto.getDescription(), mediaCardDto.getGenres(), mediaCardDto.getRating()))
                .collect(Collectors.toList());

        var viewModel = new SearchViewModel(base, filter, mediaViewModel, mediaPage.getTotalPages());

        model.addAttribute("model", viewModel);
        return "search";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
