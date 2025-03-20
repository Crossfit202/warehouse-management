package com.jonathans.DTOS;

public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String role; // Now a single role string like "ROLE_USER"

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

    public UserRequestDTO(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserRequestDTO() {
    }

    @Override
    public String toString() {
        return "UserRequestDTO [username=" + username + ", email=" + email + ", password=" + password + ", role=" + role
                + "]";
    }

}
