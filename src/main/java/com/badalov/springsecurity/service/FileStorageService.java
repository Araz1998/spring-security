package com.badalov.springsecurity.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    ResponseEntity<?> saveUserPhoto(MultipartFile file, String userName);

    ResponseEntity<?> updateUserPhoto(MultipartFile file, String userName);

    HttpEntity<byte[]> getUserPhotoByUserId(Long userId) throws IOException;
}
