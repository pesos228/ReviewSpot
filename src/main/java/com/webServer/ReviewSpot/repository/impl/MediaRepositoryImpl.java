package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Genre;
import com.webServer.ReviewSpot.entity.Media;
import com.webServer.ReviewSpot.repository.MediaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class MediaRepositoryImpl implements MediaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Media media) {
        if (entityManager.contains(media)){
            entityManager.merge(media);
        }else{
            entityManager.persist(media);
        }
    }

    @Override
    public Media findByPhotoUrl(String url) {
        try {
            return entityManager.createQuery("SELECT m FROM Media m WHERE m.photoUrl = :url", Media.class)
                    .setParameter("url", url)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Media findByName(String name) {
        try {
            return entityManager.createQuery("SELECT m FROM Media m WHERE m.name = :name", Media.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Media> findByNameContaining(String namePart) {
        return entityManager.createQuery("SELECT DISTINCT m FROM Media m WHERE LOWER(m.name) LIKE :namePart", Media.class)
                .setParameter("namePart","%" + namePart.toLowerCase().trim() + "%")
                .getResultList();
    }

    @Override
    public Page<Media> findByNameContaining(String namePart, Pageable pageable) {
        Long mediaCount;
        try {
            mediaCount = entityManager.createQuery("SELECT COUNT(DISTINCT m) FROM Media m WHERE LOWER(m.name) LIKE :namePart", Long.class)
                    .setParameter("namePart","%" + namePart.toLowerCase().trim() + "%")
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var mediaList = entityManager.createQuery("SELECT DISTINCT m FROM Media m WHERE LOWER(m.name) LIKE :namePart", Media.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .setParameter("namePart","%" + namePart.toLowerCase().trim() + "%")
                .getResultList();

        return new PageImpl<>(mediaList, pageable, mediaCount);
    }

    @Override
    public List<Media> findByGenres(List<Genre> genreList) {
        return entityManager.createQuery("SELECT DISTINCT m FROM Media m JOIN m.genreList g WHERE g IN :genreList GROUP BY m HAVING COUNT(DISTINCT g) = :genreCount",Media.class)
                .setParameter("genreList", genreList)
                .setParameter("genreCount", genreList.size())
                .getResultList();
    }

    @Override
    public Page<Media> findByGenres(List<Genre> genreList, Pageable pageable) {
        Long mediaCount;
        try {
            mediaCount = entityManager.createQuery("SELECT COUNT(DISTINCT m) FROM Media m JOIN m.genreList g WHERE g IN :genreList GROUP BY m HAVING COUNT(DISTINCT g) = :genreCount",Long.class)
                    .setParameter("genreList", genreList)
                    .setParameter("genreCount", genreList.size())
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var mediaList = entityManager.createQuery("SELECT DISTINCT m FROM Media m JOIN m.genreList g WHERE g IN :genreList GROUP BY m HAVING COUNT(DISTINCT g) = :genreCount",Media.class)
                .setParameter("genreList", genreList)
                .setParameter("genreCount", genreList.size())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(mediaList, pageable, mediaCount);
    }

    @Override
    public Media findById(int mediaId) {
        try {
            return entityManager.createQuery("SELECT m FROM Media m WHERE m.id = :id", Media.class)
                    .setParameter("id", mediaId)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }

    }

    @Override
    public List<Media> findAll() {
        return entityManager.createQuery("SELECT m FROM Media m", Media.class)
                .getResultList();
    }

    @Override
    public Page<Media> findAll(Pageable pageable) {
        Long mediaCount;
        try {
            mediaCount = entityManager.createQuery("SELECT COUNT(m) FROM Media m", Long.class)
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        var mediaList = entityManager.createQuery("SELECT m FROM Media m", Media.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(mediaList, pageable, mediaCount);
    }

    @Override
    public Page<Media> findByNameContainingAndByGenres(String namePart, List<Genre> genreList, Pageable pageable) {
        Long mediaCount;
        try {
            mediaCount = entityManager.createQuery("SELECT COUNT(DISTINCT m) FROM Media m JOIN m.genreList g WHERE LOWER(m.name) LIKE :namePart AND g IN :genreList " +
                    "GROUP BY m HAVING COUNT(DISTINCT g) = :genreCount", Long.class)
                    .setParameter("genreList", genreList)
                    .setParameter("genreCount", genreList.size())
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var mediaList = entityManager.createQuery("SELECT DISTINCT m FROM Media m JOIN m.genreList g WHERE LOWER(m.name) LIKE :namePart AND g IN :genreList " +
                "GROUP BY m HAVING COUNT(DISTINCT g) = :genreCount", Media.class)
                .setParameter("namePart", "%" + namePart.toLowerCase().trim() + "%")
                .setParameter("genreList", genreList)
                .setParameter("genreCount", genreList.size())
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(mediaList, pageable, mediaCount);
    }

    @Override
    public void deleteById(int id) {
        var media = findById(id);
        if (media != null){
            entityManager.remove(media);
        }
    }
}
