package com.example.springbootapp.board.controller;

import com.example.springbootapp.board.dto.BoardRequestDto;
import com.example.springbootapp.board.dto.BoardResponseDto;
import com.example.springbootapp.board.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    //  Spring 4.3 이후부터는 단일 생성자가 있는 경우 @Autowired를 생략할 수 있습니다:
    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // ResponseEntity : Spring에서 HTTP 응답을 더 세밀하게 제어할 수 있게 해주는 클래스
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto boardRequestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("User ID: " + userId);
        BoardResponseDto createdBoard = boardService.createBoard(boardRequestDto, userId);
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
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        BoardResponseDto updatedBoard = boardService.updateBoard(id, boardRequestDto, userId);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        boardService.deleteBoard(id, userId);
//        HTTP 응답으로 204 No Content 상태 코드를 반환할 때 사용하는 코드입니다.
//        대부분의 경우, 리소스를 성공적으로 삭제하거나 수정했지만 추가로 반환할 내용(Body)이 없을 때 사용합니다.
//        즉, 성공했음을 나타내지만 응답 본문은 비워두는 표준적인 방법
        return ResponseEntity.noContent().build();
    }
}