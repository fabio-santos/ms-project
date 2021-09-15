package com.vkncode.project.controller;

import com.vkncode.project.config.SecurityConfig;
import com.vkncode.project.domain.dto.UserDTO;
import com.vkncode.project.domain.entity.User;
import com.vkncode.project.domain.repository.UserRepository;
import com.vkncode.project.domain.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AuthService tokenService;
    private final UserRepository userRepository;
    private final SecurityConfig security;

    @PostMapping("/signin")
    public ResponseEntity<String> auth(@Valid @RequestBody UserDTO userDTO) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userDTO.toAuthToken();

        try {
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            String token = this.tokenService.generateJwt(authenticate);
            String fullToken = "Bearer " + token;
            return ResponseEntity.ok(fullToken);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/signup")
    public void registerUser(@Valid @RequestBody UserDTO userDTO) {
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException(userDTO.getEmail() + " already registered.");
        }

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(security.encoder().encode(userDTO.getPassword()))
                .build();

        userRepository.save(user);
    }

}