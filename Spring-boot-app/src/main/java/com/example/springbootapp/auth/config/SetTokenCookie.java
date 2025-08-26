package com.example.springbootapp.auth.config;

import com.example.springbootapp.auth.dto.JwtToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class SetTokenCookie {
    public void setTokenCookie(JwtToken jwtToken, HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", jwtToken.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(1000*60*60); // 2초 - refresh 토큰 테스트용
        response.addCookie(accessTokenCookie);

        // Refresh Token을 HttpOnly 쿠키로 설정
        Cookie refreshTokenCookie = new Cookie("refreshToken", jwtToken.getRefreshToken());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false); // 개발환경에서는 HTTP도 허용
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(7 * 24 * 60 * 60); // 7일
        response.addCookie(refreshTokenCookie);
    }
}
