package com.webServer.ReviewSpot.service.impl;

import com.webServer.ReviewSpot.dto.GenreInputDto;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import com.webServer.ReviewSpot.entity.Genre;
import com.webServer.ReviewSpot.exceptions.GenreAlreadyExistsException;
import com.webServer.ReviewSpot.exceptions.GenreNotFoundException;
import com.webServer.ReviewSpot.repository.GenreRepository;
import com.webServer.ReviewSpot.service.GenreService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService{

    private final GenreRepository genreRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public GenreServiceImpl(GenreRepository genreRepository, ModelMapper modelMapper) {
        this.genreRepository = genreRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public void save(GenreInputDto genreInputDto) {
        Genre genre = genreRepository.findByName(genreInputDto.getName());
        if (genre != null){
            throw new GenreAlreadyExistsException("Genre with name: " + genreInputDto.getName() + " already exists");
        }
        genreRepository.save(modelMapper.map(genreInputDto, Genre.class));
    }

    @Override
    public List<GenreOutputDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genre -> modelMapper.map(genre, GenreOutputDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public GenreOutputDto findById(int id) {
        var genre = genreRepository.findById(id);
        if (genre == null){
            throw new GenreNotFoundException("Genre with ID: " + id + " not found");
        }
        return modelMapper.map(genre, GenreOutputDto.class);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        var genre = genreRepository.findById(id);
        if (genre == null){
            throw new GenreNotFoundException("Genre with ID: " + id + " not found");
        }
        genreRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void update(int id, String name) {
        var genre = genreRepository.findById(id);
        if (genre == null){
            throw new GenreNotFoundException("Genre with id: " + id + " not found");
        }

        genre.setName(name);
        genreRepository.save(genre);
    }


    @Override
    public Page<GenreOutputDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        var genres = genreRepository.findAll(pageable);

        return genres.map(genre -> new GenreOutputDto(genre.getName(), genre.getId()));
    }
}
