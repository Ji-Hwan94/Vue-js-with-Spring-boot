package com.example.springbootapp.file.service;

import com.example.springbootapp.file.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    FileDto saveFile(MultipartFile file, Long boardId);
    List<FileDto> saveFiles(List<MultipartFile> files, Long boardId);
    List<FileDto> getFilesByBoardId(Long boardId);
    FileDto getFileById(Long fileId);
    void deleteFilesByBoardId(Long boardId);
    boolean deleteFile(Long fileId);
}