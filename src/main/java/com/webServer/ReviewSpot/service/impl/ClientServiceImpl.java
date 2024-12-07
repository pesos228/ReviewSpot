package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.dto.ClientInfoDto;
import com.webServer.ReviewSpot.dto.ClientInputDto;
import com.webServer.ReviewSpot.dto.ClientOutputDto;
import com.webServer.ReviewSpot.entity.Client;
import com.webServer.ReviewSpot.exceptions.ClientEmailAlreadyExistsException;
import com.webServer.ReviewSpot.exceptions.ClientNotFoundException;
import com.webServer.ReviewSpot.repository.ClientRepository;
import com.webServer.ReviewSpot.service.ClientService;
import com.webServer.ReviewSpot.service.CommentService;
import com.webServer.ReviewSpot.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final CommentService commentService;
    private final ReviewService reviewService;
    private final ModelMapper modelMapper;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository, CommentService commentService, ReviewService reviewService, ModelMapper modelMapper) {
        this.clientRepository = clientRepository;
        this.commentService = commentService;
        this.reviewService = reviewService;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public void save(ClientInputDto clientInputDto) {
        Client client = clientRepository.findByEmail(clientInputDto.getEmail());
        if (client != null){
            throw new ClientEmailAlreadyExistsException("Client with email: " + clientInputDto.getEmail() + " already exists");
        }
        clientRepository.save(modelMapper.map(clientInputDto, Client.class));
    }

    @Override
    public List<ClientOutputDto> findAll() {
        return clientRepository.findAll().stream()
                .map(client -> modelMapper.map(client, ClientOutputDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClientInfoDto findById(int id) {
        Client client = clientRepository.findById(id);
        if (client == null){
            throw  new ClientNotFoundException("Client with id: " + id + " not found2");
        }
        return modelMapper.map(client, ClientInfoDto.class);

    }

    @Override
    @Transactional
    public void update(int id, String name, String photoUrl) {
        var client = clientRepository.findById(id);
        if (client == null){
            throw new ClientNotFoundException("Client with id: " + id +" not found");
        }
        client.setName(name);
        client.setPhotoUrl(photoUrl);

        clientRepository.save(client);
    }

    @Override
    public boolean authenticateClient(String email, String password) {
        Client client = clientRepository.findByEmail(email);
        if (client == null){
            throw new ClientNotFoundException("Client with email: " + email + " not found");
        }
        return client.getEmail().equals(email) && client.getPassword().equals(password);
    }

    @Override
    public List<ClientInfoDto> getMostActiveClients(int count) {
        var lastWeek = LocalDateTime.now().minusDays(7);
        return clientRepository.findAll().stream().map(
                client -> {
                    var comments = commentService.findByClientIdAfterDate(client.getId(), lastWeek);
                    var reviews = reviewService.findByClientIdAfterDate(client.getId(), lastWeek);

                    return new ClientInfoDto(client.getId(), client.getName(), client.getPhotoUrl(), comments, reviews);
                }
        ).sorted((c1, c2) -> Integer.compare(
                        c2.getComments().size() + c2.getReviews().size(),
                        c1.getComments().size() + c1.getReviews().size()
                ))
                .limit(count)
                .collect(Collectors.toList());

    }

    @Override
    public Page<ClientInfoDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var clients = clientRepository.findAll(pageable);

        return clients.map(client -> modelMapper.map(client, ClientInfoDto.class));

    }
}
