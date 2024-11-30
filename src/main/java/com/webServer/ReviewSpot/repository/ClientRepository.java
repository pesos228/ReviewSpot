package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientRepository {
    void save(Client client);
    Client findByEmail(String email);
    Client findByName(String name);
    Client findById(int id);
    List<Client> findAll();
    Page<Client> findAll(Pageable pageable);
}
