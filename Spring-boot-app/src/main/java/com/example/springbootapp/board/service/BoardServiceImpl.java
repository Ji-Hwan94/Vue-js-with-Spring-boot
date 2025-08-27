package com.example.springbootapp.board.service;

import com.example.springbootapp.board.dto.BoardRequestDto;
import com.example.springbootapp.board.dto.BoardResponseDto;
import com.example.springbootapp.file.dto.FileDto;
import com.example.springbootapp.board.repository.BoardRepository;
import com.example.springbootapp.auth.repository.UserRepository;
import com.example.springbootapp.auth.dto.UserResponseDto;
import com.example.springbootapp.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);
    
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, UserRepository userRepository, FileService fileService) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.fileService = fileService;
    }

    @Override
    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, String userId) {
        logger.debug("Creating board for user: {}", userId);
        UserResponseDto user = userRepository.findById(Long.valueOf(userId));
        if (user == null) {
            logger.error("User not found: {}", userId);
            throw new RuntimeException("User not found");
        }
        boardRequestDto.setUserId(user.getId());
        boardRepository.insertBoard(boardRequestDto);
        logger.info("Board created successfully for user: {}", userId);
        
        List<BoardResponseDto> userBoards = boardRepository.findByUserId(user.getId());
        return userBoards.get(0);
    }

    @Override
    public BoardResponseDto getBoardById(Long id) {
        BoardResponseDto board = boardRepository.findById(id);
        if (board == null) {
            throw new RuntimeException("Board not found");
        }
        
        // 파일 목록 조회
        List<FileDto> files = fileService.getFilesByBoardId(id);
        board.setFiles(files);
        
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
    public BoardResponseDto updateBoard(Long id, BoardRequestDto boardRequestDto, String userId) {
        BoardResponseDto existingBoard = boardRepository.findById(id);
        if (existingBoard == null) {
            throw new RuntimeException("Board not found");
        }
        if (!userId.equals(existingBoard.getUserId().toString())) {
            throw new RuntimeException("Only the board owner can update this board");
        }
        boardRepository.updateBoard(id, boardRequestDto);

        return boardRepository.findById(id);
    }

    @Override
    public void deleteBoard(Long id, String userId) {
        BoardResponseDto board = boardRepository.findById(id);
        if (board == null) {
            throw new RuntimeException("Board not found");
        }

        if (!userId.equals(board.getUserId().toString())) {
            throw new RuntimeException("Only the board owner can delete this board");
        }

        // 파일 삭제
        fileService.deleteFilesByBoardId(id);
        
        boardRepository.deleteById(id);
    }
}