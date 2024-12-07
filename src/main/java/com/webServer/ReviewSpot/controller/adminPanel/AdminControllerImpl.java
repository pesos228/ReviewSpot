package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminController;
import com.reviewSpot.models.viewmodel.AdminViewModelEntityList;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientPageFormModel;
import com.reviewSpot.models.viewmodel.form.genre.GenrePageFormModel;
import com.reviewSpot.models.viewmodel.form.media.MediaPageFormModel;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController {

    private final ClientService clientService;
    private final GenreService genreService;
    private final MediaService mediaService;

    @Autowired
    public AdminControllerImpl(ClientService clientService, GenreService genreService, MediaService mediaService) {
        this.clientService = clientService;
        this.genreService = genreService;
        this.mediaService = mediaService;
    }

    @Override
    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("model", new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png"),
                null, null, null, 0));
        return "admin-main";
    }

    @Override
    @GetMapping("/client")
    public String adminPanelClient(@ModelAttribute("clientForm") ClientPageFormModel clientForm, Model model) {
        var page = clientForm.clientPage() != null ? clientForm.clientPage() : 1;
        var size = clientForm.clientSize() != null ? clientForm.clientSize() : 10;
        clientForm = new ClientPageFormModel(page, size);
        var clients = clientService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png"),
                clients.stream().toList(), clientForm, "client", clients.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    @GetMapping("/genre")
    public String adminPanelGenre(@ModelAttribute("genreForm") GenrePageFormModel genreForm, Model model) {
        var page = genreForm.genrePage() != null ? genreForm.genrePage() : 1;
        var size = genreForm.genreSize() != null ? genreForm.genreSize() : 5;
        genreForm = new GenrePageFormModel(page, size);
        var genres = genreService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png"),
                genres.stream().toList(), genreForm, "genre", genres.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    @GetMapping("/media")
    public String adminPanelMedia(@ModelAttribute("mediaForm") MediaPageFormModel mediaForm, Model model) {
        var page = mediaForm.mediaPage() != null ? mediaForm.mediaPage() : 1;
        var size = mediaForm.mediaSize() != null ? mediaForm.mediaSize() : 10;
        mediaForm = new MediaPageFormModel(page, size);
        var medias = mediaService.findAll(page, size);

        var viewModel = new AdminViewModelEntityList<>(createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png"),
                medias.stream().toList(), mediaForm, "media", medias.getTotalPages());

        model.addAttribute("model", viewModel);
        return "admin-main";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
