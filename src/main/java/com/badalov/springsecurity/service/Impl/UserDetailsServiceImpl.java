package com.badalov.springsecurity.service.Impl;

import com.badalov.springsecurity.dto.MessageResponse;
import com.badalov.springsecurity.dto.UserDto;
import com.badalov.springsecurity.model.Role;
import com.badalov.springsecurity.model.User;
import com.badalov.springsecurity.repositories.RoleRepository;
import com.badalov.springsecurity.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user =  userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));

         return new org.springframework.security.core.userdetails.User(user.getUsername(),
                 user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }

    public ResponseEntity<?> registerNewUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("This username exists on DB"));
        }

        User savedUser = userRepository.save(new User(userDto.getUsername(),
                new BCryptPasswordEncoder().encode(userDto.getPassword()),
                Set.of(roleRepository.findByName("USER"))));
        if(Objects.isNull(savedUser)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Something went wrong"));
        }

        Map<Object, Object> response = new HashMap<>();
        response.put("message", "User registered successfully!");
        return ResponseEntity.ok(response);
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
