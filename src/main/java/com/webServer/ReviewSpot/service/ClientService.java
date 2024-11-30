package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.ClientInputDto;
import com.webServer.ReviewSpot.dto.ClientOutputDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {
    void save(ClientInputDto clientInputDto);
    List<ClientOutputDto> findAll();
    ClientInfoDto findById(int id);
    void update(int id, String name, String photoUrl);
    boolean authenticateClient(String email, String password);
    List<ClientInfoDto> getMostActiveClients(int count);
    Page<ClientInfoDto> findAll(int page, int size);
}
