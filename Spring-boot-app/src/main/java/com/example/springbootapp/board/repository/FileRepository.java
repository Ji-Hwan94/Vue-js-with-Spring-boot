package com.example.springbootapp.board.repository;

import com.example.springbootapp.file.dto.FileDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileRepository {
    void insertFile(FileDto fileDto);
    List<FileDto> findFilesByBoardId(Long boardId);
    FileDto findFileById(Long fileId);
    void deleteFile(Long fileId);
    void deleteFilesByBoardId(Long boardId);
}