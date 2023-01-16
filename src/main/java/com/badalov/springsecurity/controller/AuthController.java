package com.badalov.springsecurity.controller;


import com.badalov.springsecurity.dto.UserDto;
import com.badalov.springsecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserDto userDto) {
        return userService.loginUser(userDto);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        userService.logoutUser(request, response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        return userService.registerNewUser(userDto);
    }

    @PutMapping("/password")
    @Description("API for changing user's password. If all was okay, got message - " +
            "Password was successfully changed! and redirect URL for login page")
    public ResponseEntity<?> changePassword(@RequestBody UserDto userDto) {
        return userService.changeUserPassword(userDto);
    }

    @PutMapping("/email")
    @Description("API for changing user's Email. If all was okay, got message - " +
            "Email was successfully changed! and redirect URL for login page")
    public ResponseEntity<?> changeEmail(@RequestBody UserDto userDto) {
        return userService.changeUserEmail(userDto);
    }
}
