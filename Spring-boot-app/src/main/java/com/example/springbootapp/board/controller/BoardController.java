package com.example.springbootapp.board.controller;

import com.example.springbootapp.board.dto.BoardRequestDto;
import com.example.springbootapp.board.dto.BoardResponseDto;
import com.example.springbootapp.board.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto createdBoard = boardService.createBoard(boardRequestDto);
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardResponseDto> getBoardById(@PathVariable Long id) {
        BoardResponseDto board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
        List<BoardResponseDto> boards = boardService.getAllBoards();
        return ResponseEntity.ok(boards);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BoardResponseDto>> getBoardsByUserId(@PathVariable Long userId) {
        List<BoardResponseDto> boards = boardService.getBoardsByUserId(userId);
        return ResponseEntity.ok(boards);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardResponseDto> updateBoard(@PathVariable Long id, 
                                                       @Valid @RequestBody BoardRequestDto boardRequestDto) {
        BoardResponseDto updatedBoard = boardService.updateBoard(id, boardRequestDto);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id, @RequestParam Long userId) {
        boardService.deleteBoard(id, userId);
        return ResponseEntity.noContent().build();
    }
}