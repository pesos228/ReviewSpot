package com.webServer.ReviewSpot.controller.adminPanel;

import com.reviewSpot.models.controllers.adminPanel.AdminControllerDelete;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.exceptions.MediaNotFoundException;
import com.webServer.ReviewSpot.service.GenreService;
import com.webServer.ReviewSpot.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/delete")
public class AdminControllerDeleteImpl implements AdminControllerDelete {

    private final GenreService genreService;
    private final MediaService mediaService;

    @Autowired
    public AdminControllerDeleteImpl(GenreService genreService, MediaService mediaService) {
        this.genreService = genreService;
        this.mediaService = mediaService;
    }

    @Override
    @PostMapping("/{id}/genre")
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
    @PostMapping("/{id}/media")
    public String deleteMedia(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        try {
            mediaService.deleteById(id);
        }catch (MediaNotFoundException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Media successfully deleted!");
        return "redirect:/admin/media";
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
