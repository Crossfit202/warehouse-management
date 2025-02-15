package com.jonathans.DTOS;

import java.util.Set;

public class UserRequestDTO {

    private String username;
    private String email;
    private String password;
    private Set<String> roles; // Role names for assignment

    // Constructors
    public UserRequestDTO() {}

    public UserRequestDTO(String username, String email, String password, Set<String> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    // Getters and Setters
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
        return "UserRequestDTO{username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles + '}';
    }
}
