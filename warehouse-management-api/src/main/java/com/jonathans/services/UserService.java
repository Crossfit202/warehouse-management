package com.jonathans.services;

import com.jonathans.DTOS.UserDTO;
import com.jonathans.DTOS.UserRequestDTO;
// import com.jonathans.models.Role;
import com.jonathans.models.User;
import com.jonathans.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    // Constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET ALL USERS
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // GET USER BY ID
    public ResponseEntity<UserDTO> getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // CREATE USER
    @Transactional
    public void registerUser(UserRequestDTO userRequestDTO) {
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User newUser = new User();
        newUser.setUsername(userRequestDTO.getUsername());
        newUser.setEmail(userRequestDTO.getEmail());
        newUser.setPassword(encoder.encode(userRequestDTO.getPassword()));
        newUser.setRole("USER"); // Always default to USER

        userRepository.save(newUser);
    }

    // UPDATE USER (PUT)
    @Transactional
    public ResponseEntity<UserDTO> updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        return userRepository.findById(userId).map(existingUser -> {
            if (userRequestDTO.getUsername() != null) {
                existingUser.setUsername(userRequestDTO.getUsername());
            }
            if (userRequestDTO.getEmail() != null) {
                existingUser.setEmail(userRequestDTO.getEmail());
            }
            if (userRequestDTO.getPassword() != null) {
                existingUser.setPassword(userRequestDTO.getPassword());
            }
            if (userRequestDTO.getRole() != null) {
                String role = userRequestDTO.getRole();

                // Optional: Basic validation (just in case)
                if (!role.startsWith("ROLE_")) {
                    throw new RuntimeException("Invalid role format. Must start with ROLE_");
                }

                existingUser.setRole(role);
            }

            userRepository.save(existingUser);
            return ResponseEntity.ok(convertToDTO(existingUser));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // DELETE USER BY ID
    @Transactional
    public ResponseEntity<Void> deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Convert User Entity -> DTO
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole());
    }
}
