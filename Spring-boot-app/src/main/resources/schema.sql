-- Create database
CREATE DATABASE IF NOT EXISTS happy-job;
USE happy-job;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Boards table
CREATE TABLE IF NOT EXISTS boards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Board Files table
CREATE TABLE IF NOT EXISTS board_files (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_name VARCHAR(255) NOT NULL,
    saved_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT,
    board_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_boards_user_id ON boards(user_id);
CREATE INDEX idx_boards_created_at ON boards(created_at);
CREATE INDEX idx_board_files_board_id ON board_files(board_id);