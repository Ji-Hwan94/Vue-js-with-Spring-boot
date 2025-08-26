package com.example.springbootapp.config;

import com.example.springbootapp.auth.config.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // JWT 인증이 필요한 엔드포인트들
                .requestMatchers("GET", "/api/users").authenticated()
                .requestMatchers("GET", "/api/users/me").authenticated() // 현재 사용자 정보 조회
                .requestMatchers("POST", "/api/users/logout").authenticated() // 로그아웃
                .requestMatchers("GET", "/api/boards/**").authenticated()
                // 인증 없이 접근 가능한 엔드포인트 (회원가입, 로그인, 토큰 갱신)
                .requestMatchers("/api/users/login").permitAll()
                .requestMatchers("/api/users/refresh").permitAll()
                .requestMatchers("POST", "/api/users").permitAll() // 회원가입
                // 나머지 모든 요청은 허용 (개발용)
                .anyRequest().permitAll()
            )
            // HttpOnly 쿠키 사용을 위해 CSRF 보호 활성화
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/users/login", "/api/users/refresh", "/api/users/logout", "/api/boards/**")
            )
            // REST API를 위해 CSRF 보호 비활성화 (주석 처리)
            // .csrf(csrf -> csrf.disable())
            // 세션을 사용하지 않도록 설정 (JWT 사용)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // JWT 필터를 UsernamePasswordAuthenticationFilter 이전에 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}