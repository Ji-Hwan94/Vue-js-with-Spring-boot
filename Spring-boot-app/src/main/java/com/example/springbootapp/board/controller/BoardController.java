package com.example.springbootapp.board.controller;

import com.example.springbootapp.board.dto.BoardRequestDto;
import com.example.springbootapp.board.dto.BoardResponseDto;
import com.example.springbootapp.file.dto.FileDto;
import com.example.springbootapp.board.service.BoardService;
import com.example.springbootapp.file.service.FileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    private static final Logger logger = LoggerFactory.getLogger(BoardController.class);
    
    private final BoardService boardService;
    private final FileService fileService;

    //  Spring 4.3 이후부터는 단일 생성자가 있는 경우 @Autowired를 생략할 수 있습니다:
    @Autowired
    public BoardController(BoardService boardService, FileService fileService) {
        this.boardService = boardService;
        this.fileService = fileService;
    }

    // ResponseEntity : Spring에서 HTTP 응답을 더 세밀하게 제어할 수 있게 해주는 클래스
    @PostMapping
    public ResponseEntity<BoardResponseDto> createBoard(@Valid @RequestBody BoardRequestDto boardRequestDto) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Creating board for user: {}", userId);
        BoardResponseDto createdBoard = boardService.createBoard(boardRequestDto, userId);
        logger.info("Board created successfully with id: {}", createdBoard.getId());
        return new ResponseEntity<>(createdBoard, HttpStatus.CREATED);
    }
    
    // 파일과 함께 게시글 생성
    @PostMapping(value = "/with-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardResponseDto> createBoardWithFiles(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // BoardRequestDto 생성
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle(title);
        boardRequestDto.setDescription(description);
        
        // 게시글 생성
        BoardResponseDto createdBoard = boardService.createBoard(boardRequestDto, userId);
        
        // 파일이 있으면 저장
        if (files != null && !files.isEmpty()) {
            List<FileDto> savedFiles = fileService.saveFiles(files, createdBoard.getId());
            createdBoard.setFiles(savedFiles);
        }
        
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
    
    // 파일과 함께 게시글 수정
    @PutMapping(value = "/{id}/with-files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BoardResponseDto> updateBoardWithFiles(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "deleteFileIds", required = false) List<Long> deleteFileIds) {
        
        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            logger.info("Updating board {} for user {}", id, userId);
            
            // BoardRequestDto 생성
            BoardRequestDto boardRequestDto = new BoardRequestDto();
            boardRequestDto.setTitle(title);
            boardRequestDto.setDescription(description);
            
            // 게시글 수정
            boardService.updateBoard(id, boardRequestDto, userId);
            
            // 삭제할 파일들 처리
            if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
                logger.info("Deleting {} files", deleteFileIds.size());
                for (Long fileId : deleteFileIds) {
                    fileService.deleteFile(fileId);
                }
            }
            
            // 새로운 파일들 저장
            if (files != null && !files.isEmpty()) {
                logger.info("Saving {} new files", files.size());
                fileService.saveFiles(files, id);
            }
            
            // 최신 파일 목록과 함께 반환
            BoardResponseDto result = boardService.getBoardById(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Error updating board with files: ", e);
            throw new RuntimeException("게시글 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
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