package com.badalov.springsecurity.controller;


import com.badalov.springsecurity.config.JwtTokenProvider;
import com.badalov.springsecurity.dto.UserDto;
import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
public class MyRestController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MyRestController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(()-> new UsernameNotFoundException("User not exist"));
        String token = jwtTokenProvider.createToken(userDto.getUsername(), user.getRoles().stream().findFirst().map(role -> role.getName()).toString());
        Map<Object, Object> response = new HashMap<>();
        response.put("username", userDto.getUsername());
        response.put("token", token);

        return ResponseEntity.ok(response);

    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @GetMapping("/users/h")
    public List<User> getUsers() {
        System.out.println("Here");
        return userRepository.findAll();
    }


}
