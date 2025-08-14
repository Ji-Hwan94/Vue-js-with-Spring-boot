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
        userRepository.insertUser(userRequestDto.getUsername(), userRequestDto.getEmail(), 
                                 userRequestDto.getPassword(), now, now);
        
        return userRepository.findByUsername(userRequestDto.getUsername());
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        UserResponseDto user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        UserResponseDto user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
        UserResponseDto existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        UserResponseDto userWithSameUsername = userRepository.findByUsername(userRequestDto.getUsername());
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(id)) {
            throw new RuntimeException("Username already exists");
        }

        UserResponseDto userWithSameEmail = userRepository.findByEmail(userRequestDto.getEmail());
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(id)) {
            throw new RuntimeException("Email already exists");
        }

        userRepository.updateUser(id, userRequestDto.getUsername(), userRequestDto.getEmail(), 
                                 userRequestDto.getPassword(), LocalDateTime.now());
        
        return userRepository.findById(id);
    }

    @Override
    public void deleteUser(Long id) {
        UserResponseDto user = userRepository.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}