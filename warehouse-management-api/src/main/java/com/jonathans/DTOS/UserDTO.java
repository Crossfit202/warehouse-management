package com.jonathans.DTOS;

import java.util.Set;
import java.util.UUID;

public class UserDTO {

    private UUID id;
    private String username;
    private String email;
    private String password;  // ✅ Added password field
    private Set<String> roles;

    // Constructors
    public UserDTO() {}

    public UserDTO(UUID id, String username, String email, String password, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // Override toString() for debugging
    @Override
    public String toString() {
        return "UserDTO{id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +  // ✅ Now includes password
                ", roles=" + roles + '}';
    }
}
