package com.example.springbootapp.auth.controller;

import com.example.springbootapp.auth.config.SetTokenCookie;
import com.example.springbootapp.auth.dto.UserRequestDto;
import com.example.springbootapp.auth.dto.UserResponseDto;
import com.example.springbootapp.auth.service.UserService;
import com.example.springbootapp.auth.dto.JwtToken;
import com.example.springbootapp.auth.dto.RefreshTokenRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    private final UserService userService;
    private final SetTokenCookie setTokenCookie;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
        this.setTokenCookie = new SetTokenCookie();
    }
    
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    ResponseEntity<Void> login(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response){
        JwtToken jwtToken = userService.login(userRequestDto);
        
        // 기존 방식: JWT를 응답 바디로 반환 (주석 처리)
        // return ResponseEntity.ok(jwtToken);
        
        // Access Token을 HttpOnly 쿠키로 설정
//        Cookie accessTokenCookie = new Cookie("accessToken", jwtToken.getAccessToken());
//        accessTokenCookie.setHttpOnly(true);
//        accessTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
//        accessTokenCookie.setPath("/");
//        accessTokenCookie.setMaxAge(2); // 2초 - refresh 토큰 테스트용
//        response.addCookie(accessTokenCookie);
//
//        // Refresh Token을 HttpOnly 쿠키로 설정
//        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
//        refreshTokenCookie.setHttpOnly(true);
//        refreshTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
//        refreshTokenCookie.setPath("/");
//        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
//        response.addCookie(refreshTokenCookie);

        setTokenCookie.setTokenCookie(jwtToken, response);
        
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response) {
        try {
            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
            JwtToken newTokens = userService.refreshToken(refreshTokenRequest);
            
            // 기존 방식: JWT를 응답 바디로 반환 (주석 처리)
            // return ResponseEntity.ok(newTokens);
            
            // 새 Access Token을 HttpOnly 쿠키로 설정
//            Cookie accessTokenCookie = new Cookie("accessToken", newTokens.getAccessToken());
//            accessTokenCookie.setHttpOnly(true);
//            accessTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
//            accessTokenCookie.setPath("/");
//            accessTokenCookie.setMaxAge(2); // 2초 - refresh 토큰 테스트용
//            response.addCookie(accessTokenCookie);
//
//            // 새 Refresh Token을 HttpOnly 쿠키로 설정
//            Cookie refreshTokenCookie = new Cookie("refreshToken", newTokens.getRefreshToken());
//            refreshTokenCookie.setHttpOnly(true);
//            refreshTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
//            refreshTokenCookie.setPath("/");
//            refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
//            response.addCookie(refreshTokenCookie);

            setTokenCookie.setTokenCookie(newTokens, response);
            
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // httpOnly 쿠키에서 JWT를 검증하고 현재 사용자 정보를 반환하는 엔드포인트
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        try {
            // Spring Security의 SecurityContext에서 인증된 사용자 정보 가져오기
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getName().equals("anonymousUser")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            
            // 인증된 사용자의 이메일/ID로 사용자 정보 조회
            String userName = authentication.getName();

            // UserResponseDto getUser

            UserResponseDto currentUser = userService.getUserByUsername(userName);
            
            return ResponseEntity.ok(currentUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // HttpOnly 쿠키를 삭제하는 로그아웃 엔드포인트
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // Access Token 쿠키 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(accessTokenCookie);
        
        // Refresh Token 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0); // 즉시 만료
        response.addCookie(refreshTokenCookie);
        
        return ResponseEntity.ok().build();
    }
}