package com.badalov.springsecurity.controller;

import com.badalov.springsecurity.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadUserPhoto(@RequestParam("file") MultipartFile file,
                                             Principal principal) {
        ResponseEntity<?> responseEntity = fileStorageService
                .saveUserPhoto(file, principal.getName());
        return ResponseEntity.ok(responseEntity);
    }

    @PutMapping("/updateFile")
    public ResponseEntity<?> updateUserPhoto(@RequestParam("file") MultipartFile file,
                                             Principal principal) {
        ResponseEntity<?> responseEntity = fileStorageService
                .updateUserPhoto(file, principal.getName());
        return ResponseEntity.ok(responseEntity);
    }

}
