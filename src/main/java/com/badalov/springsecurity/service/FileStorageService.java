package com.badalov.springsecurity.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;


public interface FileStorageService {

    boolean store(MultipartFile file, Principal principal) throws IOException;
}
