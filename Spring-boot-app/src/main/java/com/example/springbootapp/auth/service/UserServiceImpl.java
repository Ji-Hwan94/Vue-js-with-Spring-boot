package com.example.springbootapp.auth.service;

import com.example.springbootapp.auth.config.JwtProvider;
import com.example.springbootapp.auth.dto.UserRequestDto;
import com.example.springbootapp.auth.dto.UserResponseDto;
import com.example.springbootapp.auth.repository.UserRepository;
import com.example.springbootapp.auth.dto.JwtToken;
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
        token.setToken(jwtProvider.generateToken(userRequestDto.getUsername()));
        return token;
    }
}