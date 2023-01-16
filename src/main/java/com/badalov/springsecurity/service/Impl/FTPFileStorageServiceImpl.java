package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.exception.FileStorageException;
import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.model.UserPhoto;
import com.badalov.springsecurity.repositories.UserPhotoRepository;
import com.badalov.springsecurity.repositories.UserRepository;
import com.badalov.springsecurity.util.CustomFileUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FTPFileStorageServiceImpl extends AbstractFileStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${date.format}")
    private String dateFormat;

    @Value("${file.name.prefix}")
    private String fileNamePrefix;


    public FTPFileStorageServiceImpl(UserRepository userRepository, UserPhotoRepository userPhotoRepository) {
        super(userRepository, userPhotoRepository);
    }

    @Override
    public ResponseEntity<?> saveUserPhoto(MultipartFile file, String userName) {
        String filePath = savePhotoOnFileDirectory(file);
        Map<Object, Object> response = new HashMap<>();
        if(filePath.isEmpty()) {
            response.put("message", "Something went wrong!");
            return ResponseEntity.badRequest().body(response);
        }
        UserPhoto userPhoto = savePhotoOnDB(filePath, userName);
        response.put("message", "Your photo was upload successfully!");
        response.put("fileId", userPhoto.getId());
        return ResponseEntity.ok(response);
    }

    @Transactional
    @Override
    public ResponseEntity<?> updateUserPhoto(MultipartFile file, String userName) {
        boolean result = deleteOldUserPhotoInDirectory(userName);
        Map<Object, Object> response = new HashMap<>();
        if(result) {
            String filePath = savePhotoOnFileDirectory(file);
            if(filePath.isEmpty()) {
                response.put("message", "Something went wrong!");
                return ResponseEntity.badRequest().body(response);
            }
            updateUserPhotoOnDB(filePath, userName);
            response.put("message", "Your photo was upload successfully!");
            return ResponseEntity.ok(response);
        }
        response.put("message", "Something went wrong!");
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Save user's photo to the server directory
     * @param userFile - user's photo
     * @return - path to file
     */
    private String savePhotoOnFileDirectory(MultipartFile userFile) {
        if (userFile.isEmpty()) {
            throw new FileStorageException("Failed to store empty file " + userFile.getOriginalFilename());
        }
        File savedFile;
        try {
            String fileExtension = userFile.getOriginalFilename().split("\\.")[1];
            savedFile = new File(uploadDir + File.separator + CustomFileUtil
                    .generateUserPhotoFileName(dateFormat, fileNamePrefix) + "." + fileExtension);
            FileUtils.touch(savedFile);
            FileUtils.writeByteArrayToFile(savedFile, userFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return savedFile.getPath();
    }

    /**
     * Finds user's photo on the table and then remove it in the directory
     * @param userName - user's name for getting a Path to file
     * @return true if file was deleted, else false
     */
    private boolean deleteOldUserPhotoInDirectory(String userName) {
        User user = userRepository.findByUsername(userName).get();

        UserPhoto userPhoto = userPhotoRepository.findByUserId(user).get();

        String imageSource = userPhoto.getImageSource();

        return deleteFile(imageSource);
    }

    private boolean deleteFile(String imageSource) {
        File oldFile = new File(imageSource);
        boolean deleteResult = false;
        if(oldFile.exists()) {
            deleteResult = oldFile.delete();
        }
        return deleteResult;
    }
}

