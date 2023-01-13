package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.config.JwtTokenProvider;
import com.badalov.springsecurity.payload.MessageResponse;
import com.badalov.springsecurity.dto.UserDto;
import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.repositories.RoleRepository;
import com.badalov.springsecurity.repositories.UserRepository;
import com.badalov.springsecurity.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private static final String USER_ROLE = "USER";


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public ResponseEntity<?> registerNewUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("This username exists on DB"));
        }

        User savedUser = userRepository.save(new User(userDto.getUsername(),
                new BCryptPasswordEncoder().encode(userDto.getPassword()),
                Set.of(roleRepository.findByName(USER_ROLE)),
                userDto.getEmail()));
        if (Objects.isNull(savedUser)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong"));
        }

        Map<Object, Object> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> loginUser(UserDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not exist"));
        String token = jwtTokenProvider.createToken(userDto.getUsername(), user.getRoles().stream().findFirst().map(role -> role.getName()).toString());
        Map<Object, Object> response = new HashMap<>();
        response.put("username", userDto.getUsername());
        response.put("accessToken", token);
        return ResponseEntity.ok(response);
    }

    @Override
    public void logoutUser(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }

    @Override
    public ResponseEntity<?> getAllUser(int pageSize, int pageNum) {
        List<User> users = userRepository.findAll(PageRequest.of(pageNum, pageSize));
        Map<Object, Object> response = new HashMap<>();
        if (users.isEmpty()) {
            response.put("message", "Not found users");
            return ResponseEntity.ok(response);
        }
        response.put("users", users);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<?> uploadUserPhoto(String fileName, MultipartFile file) {
        Map<Object, Object> response = new HashMap<>();

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                String rootPath = System.getProperty("catalina.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + fileName);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                System.out.println("Server File Location="
                        + serverFile.getAbsolutePath());
                response.put("message", "You successfully uploaded file=" + fileName);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                response.put("message", "You failed to upload: " + e.getMessage());
                ResponseEntity.badRequest().body(response);
            }
        }
        response.put("message", "You failed to upload " + fileName + " because the file was empty.");
        return ResponseEntity.badRequest().body(response);
    }
}
