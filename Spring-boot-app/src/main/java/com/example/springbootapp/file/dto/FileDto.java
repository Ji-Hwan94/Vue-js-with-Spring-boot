package com.example.springbootapp.file.dto;

public class FileDto {
    private Long id;
    private String originalName;
    private String savedName;
    private String filePath;
    private String fileType;
    private Long fileSize;
    private Long boardId;

    public FileDto() {}

    public FileDto(String originalName, String savedName, String filePath, String fileType, Long fileSize, Long boardId) {
        this.originalName = originalName;
        this.savedName = savedName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.boardId = boardId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getSavedName() {
        return savedName;
    }

    public void setSavedName(String savedName) {
        this.savedName = savedName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
}