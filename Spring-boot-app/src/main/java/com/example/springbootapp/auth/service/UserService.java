package com.example.springbootapp.auth.service;

import com.example.springbootapp.auth.dto.RefreshTokenRequest;
import com.example.springbootapp.auth.dto.UserRequestDto;
import com.example.springbootapp.auth.dto.UserResponseDto;
import com.example.springbootapp.auth.dto.JwtToken;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    List<UserResponseDto> getAllUsers();
    boolean validatePassword(String username, String rawPassword);
    JwtToken login(UserRequestDto userRequestDto);
    JwtToken refreshToken(RefreshTokenRequest refreshTokenRequest);

    UserResponseDto getUserByUsername(String username);
}