package com.example.springbootapp.user.service;

import com.example.springbootapp.user.dto.UserRequestDto;
import com.example.springbootapp.user.dto.UserResponseDto;
import com.example.springbootapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.findByEmail(userRequestDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        userRepository.insertUser(userRequestDto) ;
        
        return userRepository.findByUsername(userRequestDto.getUsername());
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll();
    }
}