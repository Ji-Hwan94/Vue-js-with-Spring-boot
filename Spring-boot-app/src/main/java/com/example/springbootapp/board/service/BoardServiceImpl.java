package com.example.springbootapp.board.service;

import com.example.springbootapp.board.dto.BoardRequestDto;
import com.example.springbootapp.board.dto.BoardResponseDto;
import com.example.springbootapp.board.repository.BoardRepository;
import com.example.springbootapp.auth.repository.UserRepository;
import com.example.springbootapp.auth.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto) {
        UserResponseDto user = userRepository.findById(boardRequestDto.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        LocalDateTime now = LocalDateTime.now();
        boardRepository.insertBoard(boardRequestDto);
        
        List<BoardResponseDto> userBoards = boardRepository.findByUserId(boardRequestDto.getUserId());
        return userBoards.get(0);
    }

    @Override
    public BoardResponseDto getBoardById(Long id) {
        BoardResponseDto board = boardRepository.findById(id);
        if (board == null) {
            throw new RuntimeException("Board not found");
        }
        return board;
    }

    @Override
    public List<BoardResponseDto> getAllBoards() {
        return boardRepository.findAll();
    }

    @Override
    public List<BoardResponseDto> getBoardsByUserId(Long userId) {
        UserResponseDto user = userRepository.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return boardRepository.findByUserId(userId);
    }

    @Override
    public BoardResponseDto updateBoard(Long id, BoardRequestDto boardRequestDto) {
        BoardResponseDto existingBoard = boardRepository.findById(id);
        if (existingBoard == null) {
            throw new RuntimeException("Board not found");
        }

        if (!existingBoard.getUserId().equals(boardRequestDto.getUserId())) {
            throw new RuntimeException("Only the board owner can update this board");
        }

        boardRepository.updateBoard(boardRequestDto);
        
        return boardRepository.findById(id);
    }

    @Override
    public void deleteBoard(Long id, Long userId) {
        BoardResponseDto board = boardRepository.findById(id);
        if (board == null) {
            throw new RuntimeException("Board not found");
        }

        if (!board.getUserId().equals(userId)) {
            throw new RuntimeException("Only the board owner can delete this board");
        }

        boardRepository.deleteById(id);
    }
}