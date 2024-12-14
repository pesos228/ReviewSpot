package com.webServer.ReviewSpot.entity;

import com.webServer.ReviewSpot.enums.ClientRoles;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {
    private ClientRoles name;

    public Role(ClientRoles name) {
        this.name = name;
    }

    protected Role(){

    }

    @Enumerated(EnumType.STRING)
    @Column(unique = true, name = "name", nullable = false)
    public ClientRoles getName() {
        return name;
    }

    public void setName(ClientRoles name) {
        this.name = name;
    }
}
