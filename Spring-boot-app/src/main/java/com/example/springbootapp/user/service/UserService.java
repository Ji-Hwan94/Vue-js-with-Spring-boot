package com.example.springbootapp.user.service;

import com.example.springbootapp.user.dto.UserRequestDto;
import com.example.springbootapp.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto getUserByUsername(String username);
    UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);
    void deleteUser(Long id);
}