package com.example.springbootapp.auth.repository;

import com.example.springbootapp.auth.dto.UserRequestDto;
import com.example.springbootapp.auth.dto.UserResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {

    UserResponseDto findById(@Param("id") Long id);

    List<UserResponseDto> findAll();

    UserResponseDto findByUsername(@Param("username") String username);

    UserResponseDto findByEmail(@Param("email") String email);

    int insertUser(UserRequestDto userRequestDto);


}