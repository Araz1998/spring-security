package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.model.UserPhoto;
import com.badalov.springsecurity.repositories.UserPhotoRepository;
import com.badalov.springsecurity.repositories.UserRepository;
import com.badalov.springsecurity.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public abstract class AbstractFileStorageService implements FileStorageService {
    protected final UserRepository userRepository;
    protected final UserPhotoRepository userPhotoRepository;

    @Autowired
    public AbstractFileStorageService(UserRepository userRepository, UserPhotoRepository userPhotoRepository) {
        this.userRepository = userRepository;
        this.userPhotoRepository = userPhotoRepository;
    }


    public UserPhoto savePhotoOnDB(String imgPath, String userName) {
        User foundUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(foundUser);
        userPhoto.setImageSource(imgPath);
        return userPhotoRepository.save(userPhoto);
    }


    public int updateUserPhotoOnDB(String imgPath, String userName) {
        User foundUser = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return userPhotoRepository.updateUserPhoto(foundUser, imgPath);
    }

    @Override
    public ResponseEntity getUserPhotoByUserId(Long userId) throws IOException {
        User user = userRepository.findById(userId).get();
        UserPhoto userPhoto = userPhotoRepository.findByUserId(user).get();
        File file = new File(userPhoto.getImageSource());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=" + file.getName())
                .body(Files.readAllBytes(file.toPath()));

    }
}
