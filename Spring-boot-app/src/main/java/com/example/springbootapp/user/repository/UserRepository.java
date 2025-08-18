package com.example.springbootapp.user.repository;

import com.example.springbootapp.user.dto.UserRequestDto;
import com.example.springbootapp.user.dto.UserResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserRepository {

    UserResponseDto findById(@Param("id") Long id);

    List<UserResponseDto> findAll();

    UserResponseDto findByUsername(@Param("username") String username);

    UserResponseDto findByEmail(@Param("email") String email);

    int insertUser(UserRequestDto userRequestDto);


}