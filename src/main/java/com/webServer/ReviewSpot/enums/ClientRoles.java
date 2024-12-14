package com.webServer.ReviewSpot.enums;

public enum ClientRoles {
    CLIENT("CLIENT"), ADMIN("ADMIN");

    private String roleName;

    ClientRoles(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
