package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Comment;
import com.webServer.ReviewSpot.repository.CommentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class CommentRepositoryImpl implements CommentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Comment comment) {
        if (entityManager.contains(comment)){
            entityManager.merge(comment);
        }else{
            entityManager.persist(comment);
        }
    }

    @Override
    public Comment findById(int id) {
        try {
            return entityManager.createQuery("SELECT c FROM Comment c WHERE c.id = :id", Comment.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Comment> findByClientId(int id) {
        return entityManager.createQuery("SELECT c FROM Comment c JOIN c.client cl WHERE cl.id = :id", Comment.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Comment> findByClientIdAfterDate(int id, LocalDateTime date) {
        return entityManager.createQuery("SELECT c FROM Comment c JOIN c.client cl WHERE cl.id = :id AND c.dateTime > :date", Comment.class)
                .setParameter("id", id)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Comment> findByMediaId(int id) {
        return entityManager.createQuery("SELECT c FROM Comment c JOIN c.media m WHERE m.id = :id", Comment.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Comment> findByMediaIdAfterDate(int id, LocalDateTime date) {
        return entityManager.createQuery("SELECT c FROM Comment c JOIN c.media m WHERE m.id = :id AND c.dateTime > :date", Comment.class)
                .setParameter("id", id)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Comment> getLastCommentsByClientId(int id, int count) {
        return entityManager.createQuery(
                        "SELECT c FROM Comment c JOIN c.client cl WHERE cl.id = :id ORDER BY c.dateTime DESC",
                        Comment.class)
                .setParameter("id", id)
                .setMaxResults(count)
                .getResultList();
    }

    @Override
    public Page<Comment> getLastCommentsByMediaId(int id, Pageable pageable) {
        Long commentCount;
        try {
            commentCount = entityManager.createQuery("SELECT COUNT(c) FROM Comment c JOIN c.media m WHERE m.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var comments = entityManager.createQuery("SELECT c FROM Comment c JOIN c.media m WHERE m.id = :id ORDER BY c.dateTime DESC", Comment.class)
                .setParameter("id", id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(comments, pageable, commentCount);
    }


    @Override
    public void deleteById(int id) {
        var comment = findById(id);
        if (comment != null){
            entityManager.remove(comment);
        }
    }

    @Override
    public Page<Comment> getLastCommentsByClientId(int id, Pageable pageable) {
        Long commentCount;
        try {
            commentCount = entityManager.createQuery("SELECT COUNT(c) FROM Comment c JOIN c.client cl WHERE cl.id = :id", Long.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        var comments = entityManager.createQuery("SELECT c FROM Comment c JOIN c.client cl WHERE cl.id = :id", Comment.class)
                .setParameter("id", id)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(comments, pageable, commentCount);
    }
}
