package com.example.springbootapp.board.service;

import com.example.springbootapp.board.dto.BoardRequestDto;
import com.example.springbootapp.board.dto.BoardResponseDto;

import java.util.List;

public interface BoardService {
    BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String userId);
    BoardResponseDto getBoardById(Long id);
    List<BoardResponseDto> getAllBoards();
    List<BoardResponseDto> getBoardsByUserId(Long userId);
    BoardResponseDto updateBoard(Long id, BoardRequestDto boardRequestDto, String userId);
    void deleteBoard(Long id, String userId);
}