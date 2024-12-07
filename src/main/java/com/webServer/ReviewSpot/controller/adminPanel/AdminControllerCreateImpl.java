package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminControllerCreate;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientFormModel;
import com.reviewSpot.models.viewmodel.form.genre.GenreFormModel;
import com.reviewSpot.models.viewmodel.form.media.MediaFormModel;
import com.webServer.ReviewSpot.dto.ClientInputDto;
import com.webServer.ReviewSpot.dto.GenreInputDto;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import com.webServer.ReviewSpot.dto.MediaInputDto;
import com.webServer.ReviewSpot.exceptions.ClientEmailAlreadyExistsException;
import com.webServer.ReviewSpot.exceptions.GenreAlreadyExistsException;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaAlreadyExistsException;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/create")
public class AdminControllerCreateImpl implements AdminControllerCreate {

    private final ClientService clientService;
    private final GenreService genreService;
    private final MediaService mediaService;

    @Autowired
    public AdminControllerCreateImpl(ClientService clientService, GenreService genreService, MediaService mediaService) {
        this.clientService = clientService;
        this.genreService = genreService;
        this.mediaService = mediaService;
    }

    @Override
    @GetMapping("/client")
    public String createClient(Model model) {
        var base = createBaseViewModel("Client create page", 1, null, null);
        model.addAttribute("model", base);
        model.addAttribute("entity", "client");

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new ClientFormModel(null, null, null, null));
        }

        return "admin-create";
    }

    @Override
    @GetMapping("/genre")
    public String createGenre(Model model) {
        var base = createBaseViewModel("Client create page", 1, null, null);
        model.addAttribute("model", base);
        model.addAttribute("entity", "genre");

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new GenreFormModel(null));
        }

        return "admin-create";
    }

    @Override
    @GetMapping("/media")
    public String createMedia(Model model) {
        var base = createBaseViewModel("Client create page", 1, null, null);
        model.addAttribute("model", base);
        model.addAttribute("entity", "media");
        model.addAttribute("genres", genreService.findAll().stream().map(GenreOutputDto::getName).toList());

        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new MediaFormModel(null, null, null, null));
        }

        return "admin-create";
    }

    @Override
    @PostMapping("/client")
    public String createClient(@Valid @ModelAttribute("clientForm") ClientFormModel clientFormModel,
                               BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        var base = createBaseViewModel("Client create page", 1, null, null);

        model.addAttribute("model", base);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", clientFormModel);
            return "redirect:/admin/create/client";
        }
        try {
            clientService.save(new ClientInputDto(clientFormModel.name(), clientFormModel.email(), clientFormModel.password(), clientFormModel.photoUrl()));
        }catch (ClientEmailAlreadyExistsException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("form", clientFormModel);
            return "redirect:/admin/create/client";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Client created successfully!");
        return "redirect:/admin/client";
    }

    @Override
    @PostMapping("/genre")
    public String createGenre(GenreFormModel genreFormModel, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        var base = createBaseViewModel("Genre create page", 1, null, null);

        model.addAttribute("model", base);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", genreFormModel);
            return "redirect:/admin/create/genre";
        }
        try {
            genreService.save(new GenreInputDto(genreFormModel.name()));
        }catch (GenreAlreadyExistsException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("form", genreFormModel);
            return "redirect:/admin/create/genre";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Genre created successfully!");
        return "redirect:/admin/genre";
    }

    @Override
    @PostMapping("/media")
    public String createMedia(MediaFormModel mediaFormModel, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        var base = createBaseViewModel("Media create page", 1, null, null);

        model.addAttribute("model", base);
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", mediaFormModel);
            return "redirect:/admin/create/genre";
        }
        try {
            mediaService.save(new MediaInputDto(mediaFormModel.name(), mediaFormModel.photoUrl(), mediaFormModel.description(),
                    mediaFormModel.genres()));
        }catch (MediaAlreadyExistsException | GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            redirectAttributes.addFlashAttribute("form", mediaFormModel);
            return "redirect:/admin/create/media";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Media created successfully!");
        return "redirect:/admin/media";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }

}
