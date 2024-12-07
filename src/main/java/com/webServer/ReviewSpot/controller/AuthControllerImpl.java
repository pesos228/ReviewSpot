package com.webServer.ReviewSpot.controller;

import com.reviewSpot.models.controllers.AuthController;
import com.reviewSpot.models.viewmodel.card.BaseViewModel;
import com.reviewSpot.models.viewmodel.form.client.ClientFormModel;
import com.reviewSpot.models.viewmodel.form.client.ClientLoginFormModel;
import com.webServer.ReviewSpot.dto.ClientInputDto;
import com.webServer.ReviewSpot.exceptions.ClientEmailAlreadyExistsException;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String pageLogin(Model model) {
        var baseView = createBaseViewModel("Authorization", -1, null, null);
        model.addAttribute("model", baseView);
        model.addAttribute("loginForm", new ClientLoginFormModel(null, null));
        return "auth-login";
    }

    @Override
    @GetMapping("/register")
    public String pageRegistration(Model model) {
        var baseView = createBaseViewModel("Registration", -1, null, null);
        model.addAttribute("model", baseView);
        model.addAttribute("newClient", new ClientFormModel(null, null, null, null));
        return "auth-reg";
    }

    @Override
    @PostMapping("/login")
    public String requestLogin(@Valid @ModelAttribute("loginForm") ClientLoginFormModel loginForm,
                               BindingResult bindingResult, Model model) {
        var baseView = createBaseViewModel("Authorization", -1, null, null);
        model.addAttribute("model", baseView);

        if (bindingResult.hasErrors()){
            model.addAttribute("error", "Error in filling out the form");
            return "auth-login";
        }

        try {
            if (!clientService.authenticateClient(loginForm.email(), loginForm.password())){
                model.addAttribute("error", "Invalid login or password");
                return "auth-login";
            }
        }catch (ClientNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "auth-login";
        }


        return "redirect:/";
    }

    @Override
    @PostMapping("/register")
    public String requestRegister(@Valid @ModelAttribute("newClient") ClientFormModel clientFormModel,
                                  BindingResult bindingResult, Model model) {
        var baseView = createBaseViewModel("Registration", -1, null, null);
        model.addAttribute("model", baseView);

        if (bindingResult.hasErrors()){
            model.addAttribute("error", "Error in filling out the form");
            return "auth-reg";
        }

        try {
            clientService.save(new ClientInputDto(clientFormModel.name(),
                    clientFormModel.email(),
                    clientFormModel.password(),
                    clientFormModel.photoUrl()));
            return "redirect:/";
        }catch (ClientEmailAlreadyExistsException e){
            model.addAttribute("error", e.getMessage());
            return "auth-reg";
        }
    }

    @Override
    public BaseViewModel createBaseViewModel(String title, int id, String clientName, String clientPhotoUrl) {
        return new BaseViewModel(title, id, clientName, clientPhotoUrl);
    }
}
