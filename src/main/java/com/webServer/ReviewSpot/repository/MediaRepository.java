package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Genre;
import com.webServer.ReviewSpot.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MediaRepository {
    void save(Media media);
    Media findByPhotoUrl(String url);
    Media findByName(String name);
    List<Media> findByNameContaining(String namePart);
    Page<Media> findByNameContaining(String namePart, Pageable pageable);
    List<Media> findByGenres(List<Genre> genreList);
    Page<Media> findByGenres(List<Genre> genreList, Pageable pageable);
    Media findById(int mediaId);
    List<Media> findAll();
    Page<Media> findAll(Pageable pageable);
    Page<Media> findByNameContainingAndByGenres(String namePart, List<Genre> genreList, Pageable pageable);
    void deleteById(int id);
}
