package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Genre;
import com.webServer.ReviewSpot.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Genre genre) {
        if (entityManager.contains(genre)){
            entityManager.merge(genre);
        }else{
            entityManager.persist(genre);
        }
    }

    @Override
    public Genre findByName(String name) {
        try {
            return entityManager.createQuery("SELECT g FROM Genre g WHERE LOWER(g.name) = LOWER(:name)", Genre.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Genre> findAll() {
        return entityManager.createQuery("SELECT g FROM Genre g", Genre.class)
                .getResultList();
    }

    @Override
    public Page<Genre> findAll(Pageable pageable) {
        var genreCount = entityManager.createQuery("SELECT COUNT(g) FROM Genre g", Long.class)
                .getSingleResult();

        var genres = entityManager.createQuery("SELECT g FROM Genre g", Genre.class)
                .getResultList();

        return new PageImpl<>(genres, pageable, genreCount);
    }

    @Override
    public Genre findById(int id) {
        try {
            return entityManager.createQuery("SELECT g FROM Genre g WHERE g.id = :id", Genre.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void deleteById(int id) {
        var genre = findById(id);
        if (genre != null){
            entityManager.remove(genre);
        }
    }
}
