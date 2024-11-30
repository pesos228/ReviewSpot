package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreRepository {
    void save(Genre genre);
    Genre findByName(String name);
    List<Genre> findAll();
    Page<Genre> findAll(Pageable pageable);
    Genre findById(int id);
    void deleteById(int id);
}
