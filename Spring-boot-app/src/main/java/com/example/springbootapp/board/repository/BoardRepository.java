package com.example.springbootapp.board.repository;

import com.example.springbootapp.board.dto.BoardRequestDto;
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

    int insertBoard(BoardRequestDto boardRequestDto);

    int updateBoard(BoardRequestDto boardRequestDto);

    int deleteById(@Param("id") Long id);
}