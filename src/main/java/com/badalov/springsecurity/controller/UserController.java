package com.badalov.springsecurity.controller;

import com.badalov.springsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController()
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> getAllUsers(@RequestBody Map<String, String> userMap) {
        int pageSize = Integer.parseInt(userMap.get("pageSize"));
        int pageNum = Integer.parseInt(userMap.get("pageNum"));
        return userService.getAllUser(pageSize, pageNum);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseBody
    public String handleException(RuntimeException e) {
        return "exception :" + e.toString();
    }
}
