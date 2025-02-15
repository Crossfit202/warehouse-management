package com.jonathans.services;

import com.jonathans.DTOS.UserDTO;
import com.jonathans.DTOS.UserRequestDTO;
import com.jonathans.models.Role;
import com.jonathans.models.User;
import com.jonathans.repositories.RoleRepository;
import com.jonathans.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    // ✅ GET ALL USERS
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ✅ GET USER BY ID
    public ResponseEntity<UserDTO> getUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(convertToDTO(user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    // ✅ CREATE USER
    @Transactional
    public ResponseEntity<UserDTO> createUser(UserRequestDTO userRequestDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        // Convert Role names to Role entities
        Set<Role> roles = new HashSet<>();
        for (String roleName : userRequestDTO.getRoles()) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role); // ✅ FIXED: Add role directly
        }

        // Create and save user
        User newUser = new User(userRequestDTO.getUsername(), userRequestDTO.getEmail(), userRequestDTO.getPassword());
        newUser.setRoles(roles);
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(newUser));
    }
    
 // ✅ UPDATE USER (PUT)
    @Transactional
    public ResponseEntity<UserDTO> updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        return userRepository.findById(userId).map(existingUser -> {
            // ✅ Update user fields only if provided in request
            if (userRequestDTO.getUsername() != null) {
                existingUser.setUsername(userRequestDTO.getUsername());
            }
            if (userRequestDTO.getEmail() != null) {
                existingUser.setEmail(userRequestDTO.getEmail());
            }
            if (userRequestDTO.getPassword() != null) {
                existingUser.setPassword(userRequestDTO.getPassword());
            }

            // ✅ Only update roles if they are provided, otherwise keep existing roles
            if (userRequestDTO.getRoles() != null && !userRequestDTO.getRoles().isEmpty()) {
                Set<Role> updatedRoles = getRolesFromNames(userRequestDTO.getRoles());
                existingUser.setRoles(updatedRoles);
            }

            // ✅ Ensure roles are never null
            if (existingUser.getRoles() == null) {
                existingUser.setRoles(new HashSet<>());
            }

            // Save updated user
            userRepository.save(existingUser);

            return ResponseEntity.ok(convertToDTO(existingUser));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    private Set<Role> getRolesFromNames(Set<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByRoleName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        return roles;
    }

	// ✅ DELETE USER BY ID
    @Transactional
    public ResponseEntity<Void> deleteUser(UUID userId) {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        userRepository.deleteById(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // ✅ Convert User to UserDTO
    private UserDTO convertToDTO(User user) {
        Set<String> roleNames = (user.getRoles() == null) ? new HashSet<>() :
            user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet());

        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), roleNames);
    }
}
