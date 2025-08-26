package com.example.springbootapp.auth.service;

import com.example.springbootapp.auth.config.JwtProvider;
import com.example.springbootapp.auth.dto.UserRequestDto;
import com.example.springbootapp.auth.dto.UserResponseDto;
import com.example.springbootapp.auth.repository.UserRepository;
import com.example.springbootapp.auth.dto.JwtToken;
import com.example.springbootapp.auth.dto.RefreshTokenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.findByEmail(userRequestDto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        // 패스워드 암호화 (BCrypt는 자동으로 솔트 생성 및 관리)
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        
        // 암호화된 패스워드로 DTO 업데이트
        userRequestDto.setPassword(encodedPassword);
        
        LocalDateTime now = LocalDateTime.now();
        userRepository.insertUser(userRequestDto);
        
        return userRepository.findByUsername(userRequestDto.getUsername());
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 비밀번호를 비교하여 옳바른 사용자인지를 검사한다.
     * @param username username
     * @param rawPassword 입력된 비밀번호
     * @return boolean 
     * @throws 사용자가 존재 하지 않으면 false를 return 함
     */
    @Override
    public boolean validatePassword(String username, String rawPassword) {
        UserResponseDto user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        
        // BCrypt를 사용하여 입력된 패스워드와 저장된 해시된 패스워드 비교
        // BCrypt.matches()는 자동으로 솔트를 처리하여 비교함
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * 로그인이 완료되면 jwt 토큰을 생성한다.
     * @param userRequestDto 로그인 정보
     * @return JwtToken 토큰
     * @throws 아이디가 존재하지 않거나, 비밀번호가 일치하는 않는 경우
     */
    @Override
    public JwtToken login(UserRequestDto userRequestDto) {
        JwtToken token = new JwtToken();
        UserResponseDto user = userRepository.findByUsername(userRequestDto.getUsername());
        if(user == null || !validatePassword(userRequestDto.getUsername(), userRequestDto.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        // 액세스 토큰과 리프레시 토큰 모두 생성 (사용자 ID 사용)
        token.setAccessToken(jwtProvider.generateToken(String.valueOf(user.getId())));
        token.setRefreshToken(jwtProvider.generateRefreshToken(String.valueOf(user.getId())));
        
        return token;
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 토큰을 발급합니다.
     * @param refreshTokenRequest 리프레시 토큰 요청
     * @return 새로운 JWT 토큰 (액세스 토큰과 리프레시 토큰)
     * @throws RuntimeException 리프레시 토큰이 유효하지 않은 경우
     */
    @Override
    public JwtToken refreshToken(RefreshTokenRequest refreshTokenRequest) {
        try {
            String[] tokens = jwtProvider.refreshTokens(refreshTokenRequest.getRefreshToken());
            
            JwtToken jwtToken = new JwtToken();
            jwtToken.setAccessToken(tokens[0]);
            jwtToken.setRefreshToken(tokens[1]);
            
            return jwtToken;
        } catch (Exception e) {
            throw new RuntimeException("Invalid refresh token");
        }
    }

    @Override
    public UserResponseDto getUserByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }
}