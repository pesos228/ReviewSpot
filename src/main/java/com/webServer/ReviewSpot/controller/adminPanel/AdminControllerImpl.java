package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminController;
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
        var base = createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");
        model.addAttribute("model", base);
        return "admin-main";
    }

    @Override
    @GetMapping("/client")
    public String adminPanelClient(@ModelAttribute("filter") ClientPageFormModel filter, Model model) {
        var page = filter.page() != null ? filter.page() : 1;
        var size = filter.size() != null ? filter.size() : 10;
        filter = new ClientPageFormModel(page, size);
        var base = createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");
        var clients = clientService.findAll(page, size);

        model.addAttribute("model", base);
        model.addAttribute("clients", clients);
        model.addAttribute("filter", filter);
        model.addAttribute("entity", "client");
        return "admin-main";
    }

    @Override
    @GetMapping("/genre")
    public String adminPanelGenre(@ModelAttribute("filter") GenrePageFormModel filter, Model model) {
        var page = filter.page() != null ? filter.page() : 1;
        var size = filter.size() != null ? filter.size() : 5;
        filter = new GenrePageFormModel(page, size);

        var base = createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");
        var genres = genreService.findAll(page, size);



        model.addAttribute("model", base);
        model.addAttribute("genres", genres);
        model.addAttribute("filter", filter);
        model.addAttribute("entity", "genre");
        return "admin-main";
    }

    @Override
    @GetMapping("/media")
    public String adminPanelMedia(@ModelAttribute("filter")MediaPageFormModel filter, Model model) {
        var page = filter.page() != null ? filter.page() : 1;
        var size = filter.size() != null ? filter.size() : 10;
        filter = new MediaPageFormModel(page, size);
        var base = createBaseViewModel("Admin panel", 2, "Testik", "https://png.pngtree.com/png-vector/20240123/ourlarge/pngtree-cute-little-orange-cat-cute-kitty-png-image_11459046.png");
        var media = mediaService.findAll(page, size);

        model.addAttribute("model", base);
        model.addAttribute("media", media);
        model.addAttribute("filter", filter);
        model.addAttribute("entity", "media");
        return "admin-main";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
