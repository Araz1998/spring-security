package com.badalov.springsecurity.controller;

import com.badalov.springsecurity.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        long fileSize = file.getSize();
        String fileName = file.getOriginalFilename();
        Map<Object, Object> response = new HashMap<>();
        if (fileStorageService.saveFile(file)) {
            response.put("username", fileName);
            response.put("accessToken", fileSize);
            return ResponseEntity.ok(response);
        }
        response.put("message", "upload-failed");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get")
    public String get() {
        return "get";
    }

}
