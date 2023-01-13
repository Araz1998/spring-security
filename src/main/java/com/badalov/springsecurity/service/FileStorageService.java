package com.badalov.springsecurity.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {


    boolean saveFile(MultipartFile file);
}
