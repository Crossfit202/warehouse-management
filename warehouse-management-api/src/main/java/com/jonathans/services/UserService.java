package com.jonathans.services;

import com.jonathans.DTOS.UserDTO;
import com.jonathans.DTOS.UserRequestDTO;
import com.jonathans.models.User;
import com.jonathans.repositories.UserRepository;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    public UserService(UserRepository userRepository, @Lazy AuthenticationManager authManager, JWTService jwtService) {
        this.jwtService = jwtService;
        this.authManager = authManager;
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
        newUser.setRole("ROLE_USER"); // Default role for new users

        userRepository.save(newUser);
    }

    // UPDATE USER
    @Transactional
    public ResponseEntity<UserDTO> updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        return userRepository.findById(userId).map(existingUser -> {
            if (userRequestDTO.getUsername() != null) {
                existingUser.setUsername(userRequestDTO.getUsername());
            }
            if (userRequestDTO.getEmail() != null) {
                existingUser.setEmail(userRequestDTO.getEmail());
            }
            // Password change logic
            if (userRequestDTO.getNewPassword() != null && !userRequestDTO.getNewPassword().isEmpty()) {
                // Require current password to match
                if (userRequestDTO.getCurrentPassword() == null ||
                        !encoder.matches(userRequestDTO.getCurrentPassword(), existingUser.getPassword())) {
                    throw new RuntimeException("Current password is incorrect");
                }
                existingUser.setPassword(encoder.encode(userRequestDTO.getNewPassword()));
            }
            if (userRequestDTO.getRole() != null) {
                List<String> validRoles = List.of("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER", "ROLE_INV_CLERK");
                if (!validRoles.contains(userRequestDTO.getRole())) {
                    throw new RuntimeException(
                            "Invalid role. Valid roles are: ROLE_USER, ROLE_ADMIN, ROLE_MANAGER, ROLE_INV_CLERK");
                }
                existingUser.setRole(userRequestDTO.getRole());
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
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    // Verify login credentials
    public ResponseEntity<Map<String, String>> verify(UserRequestDTO userRequestDTO, HttpServletResponse response) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestDTO.getUsername(),
                            userRequestDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                // Extract roles from the authenticated user
                List<String> authorities = authentication.getAuthorities()
                        .stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .collect(Collectors.toList());

                // Generate JWT with roles
                String rawRole = authorities.get(0);
                String normalizedRole = rawRole.startsWith("ROLE_") ? rawRole.substring(5) : rawRole;
                String token = jwtService.generateToken(userRequestDTO.getUsername(), normalizedRole);

                ResponseCookie cookie = ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false) // Set to true in production
                        .path("/")
                        .maxAge(3600) // 1 hour
                        .build();

                response.addHeader("Set-Cookie", cookie.toString());

                // Return a JSON response
                Map<String, String> responseBody = new HashMap<>();
                responseBody.put("message", "Login successful!");
                responseBody.put("username", userRequestDTO.getUsername());
                return ResponseEntity.ok(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
    }

    // Get a user by their email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    // Save the user in the database
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public User findOrCreateOAuthUser(String email, String name) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(name);
            newUser.setEmail(email);
            newUser.setRole("USER"); // Default role for OAuth users
            newUser.setPassword(""); // Optional: skip setting a password
            return userRepository.save(newUser);
        });
    }

}