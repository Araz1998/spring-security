package com.badalov.springsecurity.controller;


import com.badalov.springsecurity.config.JwtTokenProvider;
import com.badalov.springsecurity.dto.UserDto;
import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.repositories.UserRepository;
import com.badalov.springsecurity.service.Impl.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserDetailsServiceImpl userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider, UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
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

//    @GetMapping("/users")
//    public List<User> getUsers() {
//        System.out.println("Here");
//        return userRepository.findAll();
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        return userDetailsService.registerNewUser(userDto);
    }


}
