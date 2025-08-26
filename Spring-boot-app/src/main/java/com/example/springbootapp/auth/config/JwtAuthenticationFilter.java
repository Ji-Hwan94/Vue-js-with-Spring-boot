package com.example.springbootapp.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT 토큰을 검증하고 사용자 인증을 처리하는 필터
 * 모든 HTTP 요청에 대해 한 번씩 실행됩니다.
 * 
 * ========== HttpOnly 쿠키 방식으로 변경됨 ==========
 * 이전: Authorization 헤더에서만 Bearer 토큰 추출
 * 현재: HttpOnly 쿠키에서 먼저 토큰 추출, 없으면 Authorization 헤더에서 추출
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출하고 검증하여 사용자 인증을 처리합니다.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // Authorization 헤더에서 JWT 토큰 추출
        String token = extractTokenFromRequest(request);
        
        // 토큰이 유효하고 현재 인증된 사용자가 없는 경우 인증 처리
        if (token != null && jwtProvider.validateToken(token) && 
            SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 토큰에서 사용자 ID 추출
            String userId = jwtProvider.getUserIdFromToken(token);
            
            // UserDetails 객체 생성
            UserDetails userDetails = User.builder()
                    .username(userId)
                    .password("") // JWT 사용 시 비밀번호는 필요하지 않음
                    .authorities(new ArrayList<>()) // 권한 설정 (필요시 추가)
                    .build();
            
            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            
            // SecurityContext에 인증 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰을 추출합니다.
     * ========== HttpOnly 쿠키 방식으로 변경됨 ==========
     * 이전: Authorization 헤더에서만 Bearer 토큰 추출
     * 현재: HttpOnly 쿠키에서 먼저 찾고, 없으면 Authorization 헤더에서 찾습니다.
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 또는 null
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // 새로 추가: HttpOnly 쿠키에서 accessToken 추출 시도 (보안 강화)
        String tokenFromCookie = extractTokenFromCookies(request);
        if (tokenFromCookie != null) {
            return tokenFromCookie;
        }
        return null;
        
        // 이전 코드 (주석 처리): Authorization 헤더에서만 Bearer 토큰 추출
        // private String extractTokenFromRequest(HttpServletRequest request) {
        //     String bearerToken = request.getHeader("Authorization");
        //     if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
        //         return bearerToken.substring(7);
        //     }
        //     return null;
        // }
    }
    
    /**
//     * 새로 추가: HTTP 요청의 쿠키에서 accessToken을 추출합니다.
     * HttpOnly 쿠키 방식을 지원하기 위해 추가된 메서드
     * @param request HTTP 요청 객체
     * @return JWT 토큰 문자열 또는 null
     */
    private String extractTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // "accessToken" 이름의 HttpOnly 쿠키에서 JWT 토큰 추출
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}