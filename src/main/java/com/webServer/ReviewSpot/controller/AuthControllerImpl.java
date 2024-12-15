package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.AuthController;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientFormModel;
import com.reviewSpot.models.viewmodel.form.client.ClientLoginFormModel;
import com.webServer.ReviewSpot.dto.ClientInputDto;
import com.webServer.ReviewSpot.exceptions.ClientEmailAlreadyExistsException;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.impl.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    private final ClientService clientService;

    @Autowired
    public AuthControllerImpl(ClientService clientService) {
        this.clientService = clientService;
    }


    @Override
    @GetMapping("/login")
    public String pageLogin(@RequestParam(value = "error", required = false) String error, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        var baseView = createBaseViewModel("Authorization",userDetails);
        if (baseView.clientId() != -1){
            return "redirect:/";
        }
        model.addAttribute("model", baseView);
        if (error != null) {
            model.addAttribute("error", "Неверный логин или пароль");
        }
        model.addAttribute("loginForm", new ClientLoginFormModel(null, null));
        return "auth-login";
    }

    @Override
    @GetMapping("/register")
    public String pageRegistration(Model model) {
        var baseView = createBaseViewModel("Registration",  null);
        model.addAttribute("model", baseView);
        model.addAttribute("newClient", new ClientFormModel(null, null, null, null));
        return "auth-reg";
    }


    @Override
    @PostMapping("/register")
    public String requestRegister(@Valid @ModelAttribute("newClient") ClientFormModel clientFormModel,
                                  BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()){
            model.addAttribute("error", "Error in filling out the form");
            return "auth-reg";
        }

        try {
            clientService.save(new ClientInputDto(clientFormModel.name(),
                    clientFormModel.email(),
                    clientFormModel.password(),
                    clientFormModel.photoUrl()));
            return "redirect:/auth/login";
        }catch (ClientEmailAlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "auth-reg";
        }
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
