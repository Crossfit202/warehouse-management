package com.jonathans.controllers;

import com.jonathans.DTOS.UserRequestDTO;
import com.jonathans.services.JWTService;
import com.jonathans.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JWTService jwtService;

    public AuthController(UserService userService, JWTService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // ✅ LOGIN - Generate JWT and set cookie
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserRequestDTO userRequestDTO, HttpServletResponse response) {
        try {
            ResponseEntity<?> result = userService.verify(userRequestDTO, response);

            if (result.getStatusCode() == HttpStatus.OK) {
                // Assuming userService.verify() authenticates successfully
                String username = userRequestDTO.getUsername();
                var user = userService.getUserByUsername(username);
                String token = jwtService.generateToken(username, user.getRole());

                // Set the token in a secure HttpOnly cookie
                ResponseCookie cookie = ResponseCookie.from("jwt", token)
                        .httpOnly(true)
                        .secure(false) // Set to true in production
                        .path("/")
                        .maxAge(3600) // 1 hour
                        .build();

                response.addHeader("Set-Cookie", cookie.toString());
                return ResponseEntity.ok(Map.of("message", "Login successful!", "username", username));
            }
            return result;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    // ✅ LOGOUT - Clear the JWT cookie
    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false) // Set true in production
                .path("/")
                .maxAge(0) // Expire immediately
                .sameSite("Strict") // Prevent CSRF
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok("Logout successful");
    }

    // ✅ VERIFY - Validate JWT from HttpOnly cookie
    @GetMapping("/verify")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String token = null;

        // Extract JWT from cookies
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not found");
        }

        try {
            String username = jwtService.extractUserName(token);
            String role = userService.getUserByUsername(username).getRole();
            if (jwtService.validateToken(token, userService.getUserByUsername(username))) {
                return ResponseEntity.ok(Map.of("username", username, "role", role));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed");
        }
    }

}
