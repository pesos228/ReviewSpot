package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Client;
import com.webServer.ReviewSpot.entity.Media;
import com.webServer.ReviewSpot.entity.Review;
import com.webServer.ReviewSpot.repository.ReviewRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class ReviewRepositoryImpl implements ReviewRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Review review) {
        if (entityManager.contains(review)){
            entityManager.merge(review);
        }else{
            entityManager.persist(review);
        }
    }

    @Override
    public List<Review> findByMediaId(int id) {
        return entityManager.createQuery("SELECT r FROM Review r JOIN r.media m WHERE m.id = :id", Review.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Review> findByClientId(int id) {
        return entityManager.createQuery("SELECT r FROM Review r JOIN r.client c WHERE c.id = :id", Review.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Review> findByClientIdAfterDate(int id, LocalDateTime date) {
        return entityManager.createQuery("SELECT r FROM Review r JOIN r.client c WHERE c.id = :id AND r.dateTime > :date", Review.class)
                .setParameter("id", id)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Review> findByMediaIdAfterDate(int id, LocalDateTime date) {
        return entityManager.createQuery("SELECT r FROM Review r JOIN r.media m WHERE m.id = :id AND r.dateTime > :date", Review.class)
                .setParameter("id", id)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Review> getLastReviewsByClientId(int id, int count) {
        return entityManager.createQuery("SELECT r FROM Review r JOIN r.client c WHERE c.id = :id ORDER BY r.dateTime DESC", Review.class)
                .setParameter("id", id)
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public void deleteById(int id) {
        var review = entityManager.find(Review.class, id);
        if (review != null){
            entityManager.remove(review);
        }
    }

    @Override
    public Review findById(int id) {
        try {
            return entityManager.createQuery("SELECT r FROM Review r WHERE r.id = :id", Review.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Page<Review> getReviewsByMediaId(int id, Pageable pageable) {
        Long reviewCount;
        try {
            reviewCount = entityManager.createQuery("SELECT COUNT(r) FROM Review r JOIN r.media m WHERE m.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var reviews = entityManager.createQuery("SELECT r FROM Review r JOIN r.media m WHERE m.id = :id ORDER BY r.dateTime DESC", Review.class)
                .setParameter("id", id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(reviews, pageable, reviewCount);
    }

    @Override
    public Page<Review> getReviewsByClientId(int id, Pageable pageable) {
        Long reviewCount;
        try {
            reviewCount = entityManager.createQuery("SELECT COUNT(c) FROM Review r JOIN r.client c WHERE c.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var reviews = entityManager.createQuery("SELECT r FROM Review r JOIN r.client c WHERE c.id = :id", Review.class)
                .setParameter("id", id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(reviews, pageable, reviewCount);
    }
}
