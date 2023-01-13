package com.badalov.springsecurity.service;

import com.badalov.springsecurity.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseEntity<?> registerNewUser(UserDto userDto);

    ResponseEntity<?> loginUser(UserDto userDto);

    void logoutUser(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> getAllUser(int pageSize, int pageNum);


    ResponseEntity<?> uploadUserPhoto(String fileName, MultipartFile file);

}
