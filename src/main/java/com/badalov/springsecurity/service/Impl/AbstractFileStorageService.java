package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.model.UserPhoto;
import com.badalov.springsecurity.repositories.UserPhotoRepository;
import com.badalov.springsecurity.repositories.UserRepository;
import com.badalov.springsecurity.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractFileStorageService implements FileStorageService {
    protected final UserRepository userRepository;
    protected final UserPhotoRepository userPhotoRepository;

    @Autowired
    public AbstractFileStorageService(UserRepository userRepository, UserPhotoRepository userPhotoRepository) {
        this.userRepository = userRepository;
        this.userPhotoRepository = userPhotoRepository;
    }


    public UserPhoto savePhotoOnDB(String imgPath, String userName) {
        User byUsername = userRepository.findByUsername(userName).get();

        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(byUsername);
        userPhoto.setImageSource(imgPath);
        UserPhoto savedUserPhoto = userPhotoRepository.save(userPhoto);
        return savedUserPhoto;
    }


    public int updateUserPhotoOnDB(String imgPath, String userName) {
        User byUsername = userRepository.findByUsername(userName).get();

         return userPhotoRepository.updateUserPhoto(byUsername, imgPath);
    }
}
