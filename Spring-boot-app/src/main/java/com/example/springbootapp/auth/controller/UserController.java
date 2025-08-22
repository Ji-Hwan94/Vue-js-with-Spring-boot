package com.example.springbootapp.auth.controller;

import com.example.springbootapp.auth.dto.UserRequestDto;
import com.example.springbootapp.auth.dto.UserResponseDto;
import com.example.springbootapp.auth.service.UserService;
import com.example.springbootapp.auth.dto.JwtToken;
import com.example.springbootapp.auth.dto.RefreshTokenRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/login")
    ResponseEntity<JwtToken> login(@RequestBody UserRequestDto userRequestDto){
        JwtToken login = userService.login(userRequestDto);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtToken> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            JwtToken newTokens = userService.refreshToken(refreshTokenRequest);
            return ResponseEntity.ok(newTokens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}