package com.webServer.ReviewSpot.repository.impl;

import com.webServer.ReviewSpot.entity.Role;
import com.webServer.ReviewSpot.enums.ClientRoles;
import com.webServer.ReviewSpot.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role findByName(ClientRoles role) {
        try {
            return entityManager.createQuery("SELECT r FROM Role r WHERE r.name = :role", Role.class)
                    .setParameter("role", role)
                    .getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    @Override
    public void save(Role role) {
        if (entityManager.contains(role)){
            entityManager.merge(role);
        }else{
            entityManager.persist(role);
        }
    }

    @Override
    public List<Role> findAll() {
        return entityManager.createQuery("SELECT r FROM Role r", Role.class)
                .getResultList();
    }
}
