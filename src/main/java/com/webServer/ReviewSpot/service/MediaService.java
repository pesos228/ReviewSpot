package com.webServer.ReviewSpot.service;

import com.webServer.ReviewSpot.dto.MediaCardDto;
import com.webServer.ReviewSpot.dto.MediaInputDto;
import com.webServer.ReviewSpot.dto.MediaOutputDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MediaService {
    void save(MediaInputDto mediaInputDto);
    List<MediaCardDto> findAll();
    List<MediaCardDto> findByNameContaining(String name);
    List<MediaCardDto> findByGenres(List<String> genres);
    MediaOutputDto findById(int id);
    List<MediaOutputDto> getMostPopularMediaByLastWeek(int count);
    Page<MediaCardDto> getMedia(String searchQuery, List<String> genres, int page, int size);
    Page<MediaCardDto> findAll(int page, int size);
    void update(int id, String name, String photoUrl, String description, List<String> genre);
    void deleteById(int id);
}
