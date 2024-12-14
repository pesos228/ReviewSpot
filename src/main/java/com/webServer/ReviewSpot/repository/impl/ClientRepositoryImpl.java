package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Client;
import com.webServer.ReviewSpot.repository.ClientRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Client client) {
        if (entityManager.contains(client)){
            entityManager.merge(client);
        }else{
            entityManager.persist(client);
        }
    }

    @Override
    public Client findByEmail(String email) {
        try {
            return entityManager.createQuery("SELECT c FROM Client c WHERE LOWER(c.email) = LOWER(:email)", Client.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Client findByName(String name) {
        try {
            return entityManager.createQuery("SELECT c FROM Client c WHERE LOWER(c.name) = LOWER(:name)", Client.class)
                    .setParameter("name", name)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public Client findById(int id) {
        try {
            return entityManager.createQuery("SELECT c FROM Client c WHERE c.id = :id", Client.class)
                    .setParameter("id", id)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public List<Client> findAll() {
        return entityManager.createQuery("SELECT c FROM Client c", Client.class)
                .getResultList();
    }

    @Override
    public Page<Client> findAll(Pageable pageable) {
        var clientCount = entityManager.createQuery("SELECT COUNT(c) FROM Client c", Long.class)
                .getSingleResult();
        var clients = entityManager.createQuery("SELECT c FROM Client c", Client.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(clients, pageable, clientCount);
    }
}
