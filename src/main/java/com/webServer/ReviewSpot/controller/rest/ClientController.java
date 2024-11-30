package com.webServer.ReviewSpot.controller.rest;

import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.ClientInputDto;
import com.webServer.ReviewSpot.dto.ClientOutputDto;
import com.webServer.ReviewSpot.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/legacy/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    void saveClient(@RequestBody ClientInputDto clientInputDto){
        clientService.save(clientInputDto);
    }

    @GetMapping
    List<ClientOutputDto> findAll(){
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    ClientInfoDto getClientCard(@PathVariable int id){
        return clientService.findById(id);
    }
}
