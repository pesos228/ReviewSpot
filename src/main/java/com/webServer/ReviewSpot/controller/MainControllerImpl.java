package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.MainController;
import com.reviewSpot.models.viewmodel.MainViewModel;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.card.ClientCardViewModel;
import com.reviewSpot.models.viewmodel.card.MediaCardViewModel;
import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.MediaOutputDto;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainControllerImpl implements MainController {

    private final ClientService clientService;
    private final MediaService mediaService;

    @Autowired
    public MainControllerImpl(ClientService clientService, MediaService mediaService) {
        this.clientService = clientService;
        this.mediaService = mediaService;
    }

    @Override
    @GetMapping
    public String mainPage(Model model) {
        var baseView = createBaseViewModel("Main", null, null);
        var clientListDto = clientService.getMostActiveClients(5);
        var mediaListDto = mediaService.getMostPopularMediaByLastWeek(5);

        List<ClientCardViewModel> clientTop = new ArrayList<>();
        for (ClientInfoDto client: clientListDto){
            clientTop.add(new ClientCardViewModel(client.getName(), client.getPhotoUrl(), client.getComments().size(), client.getReviews().size()));
        }

        List<MediaCardViewModel> modelTop = new ArrayList<>();
        for (MediaOutputDto media: mediaListDto){
            modelTop.add(new MediaCardViewModel(media.getName(), media.getPhotoUrl(),
                    media.getDescription(), media.getGenre(), media.getRating()));
        }

        model.addAttribute("model", new MainViewModel(baseView, modelTop, clientTop));

        return "main";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, clientName, clientPhotoUrl);
    }
}
