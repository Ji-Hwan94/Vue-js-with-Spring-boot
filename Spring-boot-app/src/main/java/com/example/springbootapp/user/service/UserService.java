package com.example.springbootapp.user.service;

import com.example.springbootapp.user.dto.UserRequestDto;
import com.example.springbootapp.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);
    List<UserResponseDto> getAllUsers();

}