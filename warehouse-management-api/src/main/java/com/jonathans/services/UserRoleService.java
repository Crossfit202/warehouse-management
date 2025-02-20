package com.jonathans.services;

import com.jonathans.DTOS.UserRoleDTO;
import com.jonathans.models.Role;
import com.jonathans.models.User;
import com.jonathans.models.UserRole;
import com.jonathans.repositories.RoleRepository;
import com.jonathans.repositories.UserRepository;
import com.jonathans.repositories.UserRoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<UserRoleDTO> getAllUserRoles() {
        return userRoleRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public ResponseEntity<UserRoleDTO> getUserRoleById(UUID id) {
        return userRoleRepository.findById(id)
                .map(userRole -> ResponseEntity.ok(convertToDTO(userRole)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    public ResponseEntity<UserRoleDTO> createUserRole(UserRoleDTO userRoleDTO) {
        Optional<User> userOpt = userRepository.findById(userRoleDTO.getUserId());
        Optional<Role> roleOpt = roleRepository.findById(userRoleDTO.getRoleId());

        if (userOpt.isEmpty() || roleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        UserRole userRole = new UserRole(userOpt.get(), roleOpt.get());
        userRole = userRoleRepository.save(userRole);

        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(userRole));
    }

    private UserRoleDTO convertToDTO(UserRole userRole) {
        return new UserRoleDTO(
                userRole.getUserRoleId(),
                userRole.getUser().getId(),
                userRole.getUser().getUsername(),  // Include username
                userRole.getRole().getId(),
                userRole.getRole().getRoleName()   // Include role name
        );
    }
}
