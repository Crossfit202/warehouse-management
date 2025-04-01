package com.jonathans.controllers;

import com.jonathans.DTOS.UserDTO;
import com.jonathans.DTOS.UserRequestDTO;
import com.jonathans.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ GET ALL USERS
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ GET USER BY ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID userId) {
        return userService.getUserById(userId);
    }

    // ✅ CREATE NEW USER
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody UserRequestDTO userRequestDTO) {
        userService.registerUser(userRequestDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.ok(response);
    }

    // ✅ UPDATE USER (PUT)
    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable UUID userId,
            @RequestBody UserRequestDTO userRequestDTO) {
        return userService.updateUser(userId, userRequestDTO);
    }

    // ✅ DELETE USER BY ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        return userService.deleteUser(userId);
    }
}
