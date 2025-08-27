package com.example.springbootapp.file.controller;

import com.example.springbootapp.file.dto.FileDto;
import com.example.springbootapp.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            logger.info("Downloading file with id: {}", fileId);
            
            // 파일 정보 조회
            FileDto fileDto = fileService.getFileById(fileId);
            if (fileDto == null) {
                logger.error("File not found with id: {}", fileId);
                return ResponseEntity.notFound().build();
            }

            // 파일 리소스 생성
            Path filePath = Paths.get(fileDto.getFilePath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                logger.error("File not readable: {}", filePath);
                return ResponseEntity.notFound().build();
            }

            // Content-Type 결정
            String contentType = fileDto.getFileType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }

            // 파일명 UTF-8 인코딩 처리 (RFC 6266)
            String originalName = fileDto.getOriginalName();
            String encodedFilename = URLEncoder.encode(originalName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            logger.info("File downloaded successfully: {}", originalName);
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename*=UTF-8''" + encodedFilename)
                    .body(resource);

        } catch (MalformedURLException e) {
            logger.error("Malformed file path for file id: {}", fileId, e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error downloading file with id: {}", fileId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}