package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Reaction;
import com.webServer.ReviewSpot.repository.ReactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReactionRepositoryImpl implements ReactionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Reaction reaction) {
        if (entityManager.contains(reaction)){
            entityManager.merge(reaction);
        }else{
            entityManager.persist(reaction);
        }
    }

    @Override
    public List<Reaction> findByCommentId(int id) {
        return entityManager.createQuery("SELECT r FROM Reaction r WHERE r.targetType = 'COMMENT' AND r.targetId = :id", Reaction.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Reaction> findByReviewId(int id) {
        return entityManager.createQuery("SELECT r FROM Reaction r WHERE r.targetType = 'REVIEW' AND r.targetId = :id", Reaction.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public void deleteById(int id) {
        var reaction = entityManager.find(Reaction.class, id);
        if (reaction != null){
            entityManager.remove(reaction);
        }
    }

    @Override
    public Reaction findById(int id) {
        try {
            return entityManager.createQuery("SELECT r FROM Reaction r WHERE r.id = :id", Reaction.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
