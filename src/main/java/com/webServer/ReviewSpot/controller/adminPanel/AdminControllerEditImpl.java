package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminControllerEdit;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientFormModel;
import com.reviewSpot.models.viewmodel.form.genre.GenreFormModel;
import com.reviewSpot.models.viewmodel.form.media.MediaFormModel;
import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import com.webServer.ReviewSpot.dto.MediaOutputDto;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/edit")
public class AdminControllerEditImpl implements AdminControllerEdit {

    private final ClientService clientService;
    private final GenreService genreService;
    private final MediaService mediaService;

    @Autowired
    public AdminControllerEditImpl(ClientService clientService, GenreService genreService, MediaService mediaService) {
        this.clientService = clientService;
        this.genreService = genreService;
        this.mediaService = mediaService;
    }

    @Override
    @GetMapping("/{id}/client")
    public String editClient(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        var base = createBaseViewModel("Client edit page", null, null);
        ClientInfoDto client;
        try {
            client = clientService.findById(id);
        }catch (ClientNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        model.addAttribute("form", new ClientFormModel(client.getName(), null, null, client.getPhotoUrl()));
        model.addAttribute("model", model);
        model.addAttribute("entity", "client");
        return "admin-edit";
    }

    @Override
    @GetMapping("/{id}/genre")
    public String editGenre(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        var base = createBaseViewModel("Genre edit page", null, null);
        GenreOutputDto genre;
        try {
            genre = genreService.findById(id);
        }catch (GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }
        model.addAttribute("form", new GenreFormModel(genre.getName()));
        model.addAttribute("base", base);
        model.addAttribute("entity", "genre");
        return "admin-edit";
    }

    @Override
    @GetMapping("/{id}/media")
    public String editMedia(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        var base = createBaseViewModel("Media edit page", null, null);
        MediaOutputDto media;
        try {
            media = mediaService.findById(id);
        }catch (MediaNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }
        model.addAttribute("base", base);
        model.addAttribute("form", new MediaFormModel(media.getName(), media.getPhotoUrl(), media.getDescription(), media.getGenre()));
        model.addAttribute("entity", "media");

        return "admin-edit";
    }

    @Override
    @PostMapping("/{id}/client")
    public String createClient(@PathVariable int id, @Valid @ModelAttribute("clientForm") ClientFormModel clientFormModel,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", clientFormModel);
            return "redirect:/admin/edit/" + id + "/client";
        }
        try {
            clientService.update(id,clientFormModel.name(), clientFormModel.photoUrl());
        }catch (ClientNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/edit/" + id + "/client";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Client successfully updated!");
        return "redirect:/admin/client";
    }

    @Override
    @PostMapping("/{id}/genre")
    public String createGenre(@PathVariable int id, @Valid @ModelAttribute("genreForm") GenreFormModel genreFormModel,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", genreFormModel);
            return "redirect:/admin/edit/" + id + "/genre";
        }
        try {
            genreService.update(id, genreFormModel.name());
        }catch (GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/edit/" + id + "/genre";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Genre successfully updated!");
        return "redirect:/admin/genre";
    }

    @Override
    @PostMapping("/{id}/media")
    public String createMedia(@PathVariable int id, @Valid @ModelAttribute("mediaForm") MediaFormModel mediaFormModel,
                              BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", mediaFormModel);
            return "redirect:/admin/edit/" + id + "/media";
        }
        try {
            mediaService.update(id, mediaFormModel.name(), mediaFormModel.photoUrl(), mediaFormModel.description(), mediaFormModel.genres());
        }catch (MediaNotFoundException | GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/edit/" + id + "/media";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Media successfully updated!");
        return "redirect:/admin/media";
    }

    @Override
    @DeleteMapping("/{id}/genre")
    public String deleteGenre(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            genreService.deleteById(id);
        }catch (GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Genre successfully deleted!");
        return "redirect:/admin/genre";

    }

    @Override
    @DeleteMapping("/{id}/media")
    public String deleteMedia(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            mediaService.deleteById(id);
        }catch (MediaNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Media successfully deleted!");
        return "redirect:/admin/genre";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, clientName, clientPhotoUrl);
    }
}