package com.example.springbootapp.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 컴포넌트
 * JSON Web Token을 이용하여 사용자 인증 정보를 관리합니다.
 */
@Component
public class JwtProvider {
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * JWT 토큰을 생성합니다.
     * @param username 사용자명
     * @return 생성된 JWT 토큰 문자열
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)           // 토큰 주체 (사용자명)
                .issuedAt(now)               // 토큰 발급 시간
                .expiration(expiryDate)      // 토큰 만료 시간
                .signWith(getSigningKey())      // 서명키로 토큰 서명
                .compact();                     // 토큰 문자열 생성
    }

    /**
     * JWT 토큰에서 사용자명을 추출합니다.
     * @param token JWT 토큰
     * @return 사용자명
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * JWT 토큰의 유효성을 검증합니다.
     * @param token JWT 토큰
     * @return 토큰이 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 토큰이 유효하지 않은 경우 (만료, 서명 오류 등)
            return false;
        }
    }

    /**
     * JWT 토큰의 만료 여부를 확인합니다.
     * @param token JWT 토큰
     * @return 토큰이 만료되었으면 true, 그렇지 않으면 false
     */
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = getClaimsFromToken(token).getExpiration();
            return expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * JWT 토큰에서 Claims를 추출합니다.
     * @param token JWT 토큰
     * @return Claims 객체
     * @throws JwtException 토큰이 유효하지 않은 경우
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * JWT 서명을 위한 키를 생성합니다.
     * @return SecretKey 객체
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
