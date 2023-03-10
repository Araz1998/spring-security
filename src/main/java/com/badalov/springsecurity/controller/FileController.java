package com.badalov.springsecurity.controller;

import com.badalov.springsecurity.annotation.CurrentUserId;
import com.badalov.springsecurity.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadUserPhoto(@RequestParam("file") MultipartFile file,
                                             Principal principal) {
        ResponseEntity<?> responseEntity = fileStorageService
                .saveUserPhoto(file, principal.getName());
        return ResponseEntity.ok(responseEntity);
    }

    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @PutMapping("/updateFile")
    public ResponseEntity<?> updateUserPhoto(@RequestParam("file") MultipartFile file,
                                             Principal principal) {
        ResponseEntity<?> responseEntity = fileStorageService
                .updateUserPhoto(file, principal.getName());
        return ResponseEntity.ok(responseEntity);
    }

    @GetMapping("/photo")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public HttpEntity<byte[]> getUserPhoto(@CurrentUserId Long userId) throws IOException {
        return fileStorageService.getUserPhotoByUserId(userId);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseBody
    public String handleException(RuntimeException e) {
        return "exception :" + e.toString();
    }
}
