package com.webServer.ReviewSpot.repository;

import com.webServer.ReviewSpot.entity.Role;
import com.webServer.ReviewSpot.enums.ClientRoles;

import java.util.List;

public interface RoleRepository {
    Role findByName(ClientRoles role);
    void save(Role role);
    List<Role> findAll();
}
