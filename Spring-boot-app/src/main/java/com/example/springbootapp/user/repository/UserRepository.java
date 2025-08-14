package com.example.springbootapp.user.repository;

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

    int insertUser(@Param("username") String username, 
                  @Param("email") String email, 
                  @Param("password") String password,
                  @Param("createdAt") LocalDateTime createdAt,
                  @Param("updatedAt") LocalDateTime updatedAt);

    int updateUser(@Param("id") Long id,
                  @Param("username") String username, 
                  @Param("email") String email, 
                  @Param("password") String password,
                  @Param("updatedAt") LocalDateTime updatedAt);

    int deleteById(@Param("id") Long id);
}