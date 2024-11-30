package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.GenreInputDto;
import com.webServer.ReviewSpot.dto.GenreOutputDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenreService {
    void save(GenreInputDto genreInputDto);
    List<GenreOutputDto> findAll();
    GenreOutputDto findById(int id);
    void deleteById(int id);
    void update(int id, String name);
    Page<GenreOutputDto> findAll(int page, int size);
}
