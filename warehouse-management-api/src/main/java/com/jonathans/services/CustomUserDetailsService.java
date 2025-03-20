package com.jonathans.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jonathans.models.User;
import com.jonathans.models.UserPrincipal;
import com.jonathans.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Load User by username from DB
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User appUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // return org.springframework.security.core.userdetails.User.builder()
        // .username(appUser.getUsername())
        // .password(appUser.getPassword())
        // .roles(appUser.getRole())
        // .build();
        return new UserPrincipal(appUser);
    }

}