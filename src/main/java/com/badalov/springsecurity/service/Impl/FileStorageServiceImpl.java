package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.exception.FileStorageException;
import com.badalov.springsecurity.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;



    public boolean saveFile(MultipartFile file) {
        boolean result = false;
        String fileName = file.getOriginalFilename();
        File pathFile = new File(uploadDir);
        if(!pathFile.exists()) {
            pathFile.mkdir();
        }

        pathFile = new File(uploadDir + fileName);
        try {
            file.transferTo(pathFile);
            result= true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;

    }
}
