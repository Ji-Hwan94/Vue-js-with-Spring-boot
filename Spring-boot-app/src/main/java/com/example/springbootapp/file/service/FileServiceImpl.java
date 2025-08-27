package com.example.springbootapp.file.service;

import com.example.springbootapp.board.repository.FileRepository;
import com.example.springbootapp.file.dto.FileDto;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public FileDto saveFile(MultipartFile file, Long boardId) {
        if (file.isEmpty()) {
            logger.warn("Empty file provided for board {}", boardId);
            return null;
        }

        try {
            logger.info("Saving file {} for board {}", file.getOriginalFilename(), boardId);
            
            // 업로드 디렉토리 생성
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                logger.info("Creating upload directory: {}", uploadDir);
                uploadDirectory.mkdirs();
            }

            // 고유한 파일명 생성
            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);
            String uuid = UUID.randomUUID().toString();
            String savedFilename = uuid + "." + extension;

            // 파일 저장 경로 (디렉토리 구분자 확인)
            Path filePath = Paths.get(uploadDir, savedFilename);
            logger.debug("Saving file to path: {}", filePath.toString());
            Files.copy(file.getInputStream(), filePath);

            // FileDto 생성
            FileDto fileDto = new FileDto(
                originalFilename,
                savedFilename,
                filePath.toString(),
                file.getContentType(),
                file.getSize(),
                boardId
            );

            // 데이터베이스에 파일 정보 저장
            fileRepository.insertFile(fileDto);
            logger.info("File saved successfully: {}", savedFilename);

            return fileDto;
        } catch (IOException e) {
            logger.error("Error saving file: ", e);
            throw new RuntimeException("파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error saving file: ", e);
            throw new RuntimeException("파일 저장 중 예상치 못한 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @Override
    public List<FileDto> saveFiles(List<MultipartFile> files, Long boardId) {
        List<FileDto> savedFiles = new ArrayList<>();
        
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    FileDto savedFile = saveFile(file, boardId);
                    if (savedFile != null) {
                        savedFiles.add(savedFile);
                    }
                }
            }
        }
        
        return savedFiles;
    }

    @Override
    public List<FileDto> getFilesByBoardId(Long boardId) {
        return fileRepository.findFilesByBoardId(boardId);
    }

    @Override
    public FileDto getFileById(Long fileId) {
        return fileRepository.findFileById(fileId);
    }

    @Override
    public void deleteFilesByBoardId(Long boardId) {
        List<FileDto> files = fileRepository.findFilesByBoardId(boardId);
        
        // 물리적 파일 삭제
        for (FileDto fileDto : files) {
            try {
                Path filePath = Paths.get(fileDto.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                // 로그 기록 (실제 환경에서는 로거 사용)
                System.err.println("파일 삭제 실패: " + fileDto.getFilePath() + " - " + e.getMessage());
            }
        }
        
        // DB에서 파일 정보 삭제
        fileRepository.deleteFilesByBoardId(boardId);
    }

    @Override
    public boolean deleteFile(Long fileId) {
        FileDto fileDto = fileRepository.findFileById(fileId);
        if (fileDto != null) {
            try {
                // 물리적 파일 삭제
                Path filePath = Paths.get(fileDto.getFilePath());
                Files.deleteIfExists(filePath);
                
                // DB에서 파일 정보 삭제
                fileRepository.deleteFile(fileId);
                return true;
            } catch (IOException e) {
                System.err.println("파일 삭제 실패: " + fileDto.getFilePath() + " - " + e.getMessage());
                return false;
            }
        }
        return false;
    }
}