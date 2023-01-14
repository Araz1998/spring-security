package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.exception.FileStorageException;

import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.model.UserPhoto;
import com.badalov.springsecurity.repositories.UserPhotoRepository;
import com.badalov.springsecurity.repositories.UserRepository;
import com.badalov.springsecurity.service.FileStorageService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private final UserPhotoRepository userPhotoRepository;
    private final UserRepository userRepository;

    @Autowired
    public FileStorageServiceImpl(UserPhotoRepository userPhotoRepository, UserRepository userRepository) {
        this.userPhotoRepository = userPhotoRepository;
        this.userRepository = userRepository;
    }

    public boolean store(MultipartFile file, Principal principal) {
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file " + file.getOriginalFilename());
        }
        boolean saved = false;
        try {

            File savedFile = new File(uploadDir + File.separator + file.getOriginalFilename());
            FileUtils.touch(savedFile);
            FileUtils.writeByteArrayToFile(savedFile, file.getBytes());
            UserPhoto userPhoto = savePhoto(savedFile.getPath(), principal);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return saved;
    }

    private UserPhoto savePhoto(String imgPath, Principal principal) {
        String userName = principal.getName();
        User byUsername = userRepository.findByUsername(userName).get();

        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(byUsername);
        userPhoto.setImageSource(imgPath);
        UserPhoto save = userPhotoRepository.save(userPhoto);
        return save;

    }
}
