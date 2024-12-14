package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminControllerEdit;
import com.reviewSpot.models.viewmodel.AdminViewModelEntityEdit;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientEditForm;
import com.reviewSpot.models.viewmodel.form.client.ClientFormModel;
import com.reviewSpot.models.viewmodel.form.genre.GenreFormModel;
import com.reviewSpot.models.viewmodel.form.media.MediaFormModel;
import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import com.webServer.ReviewSpot.dto.MediaOutputDto;
import com.webServer.ReviewSpot.enums.ClientRoles;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.exceptions.RoleNotFoundException;
import com.webServer.ReviewSpot.repository.RoleRepository;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RoleRepository roleRepository;

    @Autowired
    public AdminControllerEditImpl(ClientService clientService, GenreService genreService, MediaService mediaService, RoleRepository roleRepository) {
        this.clientService = clientService;
        this.genreService = genreService;
        this.mediaService = mediaService;
        this.roleRepository = roleRepository;
    }

    @Override
    @GetMapping("/{id}/client")
    public String editClient(@PathVariable int id, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        ClientInfoDto client;
        try {
            client = clientService.findById(id);
        }catch (ClientNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        var viewModel = new AdminViewModelEntityEdit(createBaseViewModel("Client edit page", userDetails), "client", id);

        model.addAttribute("model", viewModel);
        model.addAttribute("clientForm",new ClientEditForm(client.getName(), client.getPhotoUrl(), client.getRole()));
        model.addAttribute("roles", roleRepository.findAll());
        return "admin-edit";
    }

    @Override
    @GetMapping("/{id}/genre")
    public String editGenre(@PathVariable int id, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        GenreOutputDto genre;
        try {
            genre = genreService.findById(id);
        }catch (GenreNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        var viewModel = new AdminViewModelEntityEdit(createBaseViewModel("Genre edit page", userDetails),
                "genre", id);

        model.addAttribute("model", viewModel);
        model.addAttribute("genreForm", new GenreFormModel(genre.getName()));
        return "admin-edit";
    }

    @Override
    @GetMapping("/{id}/media")
    public String editMedia(@PathVariable int id, Model model, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        MediaOutputDto media;
        try {
            media = mediaService.findById(id);
        }catch (MediaNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        var viewModel = new AdminViewModelEntityEdit(createBaseViewModel("Media edit page", userDetails),
                "media", id);

        model.addAttribute("model", viewModel);
        model.addAttribute("mediaForm", new MediaFormModel(media.getName(), media.getPhotoUrl(), media.getDescription(), media.getGenre()));
        model.addAttribute("genres", genreService.findAll().stream().map(GenreOutputDto::getName).toList());

        return "admin-edit";
    }

    @Override
    @PostMapping("/{id}/client")
    public String createClient(@PathVariable int id, @Valid @ModelAttribute("clientForm") ClientEditForm clientEditForm,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("error", "Error in filling out the form");
            redirectAttributes.addFlashAttribute("form", clientEditForm);
            return "redirect:/admin/edit/" + id + "/client";
        }
        try {
            clientService.update(id,clientEditForm.name(), clientEditForm.photoUrl(), ClientRoles.valueOf(clientEditForm.role()));
        }catch (ClientNotFoundException | RoleNotFoundException e){
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
