package com.example.springbootapp.board.repository;

import com.example.springbootapp.board.dto.BoardResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BoardRepository {

    BoardResponseDto findById(@Param("id") Long id);

    List<BoardResponseDto> findAll();

    List<BoardResponseDto> findByUserId(@Param("userId") Long userId);

    int insertBoard(@Param("title") String title,
                   @Param("description") String description,
                   @Param("userId") Long userId,
                   @Param("createdAt") LocalDateTime createdAt,
                   @Param("updatedAt") LocalDateTime updatedAt);

    int updateBoard(@Param("id") Long id,
                   @Param("title") String title,
                   @Param("description") String description,
                   @Param("updatedAt") LocalDateTime updatedAt);

    int deleteById(@Param("id") Long id);
}