// package com.jonathans.security;

// import org.springframework.security.core.Authentication;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import
// org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.stereotype.Component;

// import com.jonathans.models.User;
// import com.jonathans.services.UserService;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// import java.io.IOException;

// import org.springframework.context.annotation.Lazy;
// import
// org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;

// @Component
// public class OAuth2AuthenticationSuccessHandler implements
// AuthenticationSuccessHandler {

// private final UserService userService;

// public OAuth2AuthenticationSuccessHandler(@Lazy UserService userService) {
// this.userService = userService;
// }

// @Override
// public void onAuthenticationSuccess(HttpServletRequest request,
// HttpServletResponse response,
// Authentication authentication) throws IOException, ServletException {

// OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
// String username = oauth2User.getName(); // OAuth2 username
// String email = oauth2User.getAttribute("email"); // Extract the email if
// present

// // Check if the user exists in the database
// User existingUser = userService.getUserByEmail(email);
// if (existingUser == null) {
// // User doesn't exist, so create a new user
// User newUser = new User();
// newUser.setUsername(username);
// newUser.setEmail(email);
// newUser.setPassword(""); // OAuth users typically don't need a password
// newUser.setRole("USER"); // Default role, you can adjust this based on logic
// userService.saveUser(newUser); // Save the user in the database
// }

// // Retrieve the user from the database (with roles)
// User user = userService.getUserByEmail(email);
// if (user != null) {
// // Create a token and set it in the security context
// UsernamePasswordAuthenticationToken authenticationToken = new
// UsernamePasswordAuthenticationToken(
// user, null, user.getAuthorities());
// authenticationToken.setDetails(oauth2User.getAttributes());
// SecurityContextHolder.getContext().setAuthentication(authenticationToken);

// // Redirect after successful login
// response.sendRedirect("/users"); // Or your desired redirect URL
// }
// }
// }
