package com.jonathans.DTOS;

import java.util.UUID;

public class UserRoleDTO {

    private UUID userRoleId;
    private UUID userId;
    private String username;
    private UUID roleId;
    private String roleName;

    public UserRoleDTO() {}

    public UserRoleDTO(UUID userRoleId, UUID userId, String username, UUID roleId, String roleName) {
        this.userRoleId = userRoleId;
        this.userId = userId;
        this.username = username;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public UUID getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(UUID userRoleId) {
        this.userRoleId = userRoleId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
