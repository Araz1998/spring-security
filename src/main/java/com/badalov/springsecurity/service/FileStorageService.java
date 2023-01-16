package com.badalov.springsecurity.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;


public interface FileStorageService {

    ResponseEntity<?> saveUserPhoto(MultipartFile file, String userName);

    ResponseEntity<?> updateUserPhoto(MultipartFile file, String userName);
}
