package com.example.springbootapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt는 자동으로 솔트를 생성하고 관리함
        // strength: 4~31, 기본값 10 (높을수록 보안 강화, 속도 저하)
        return new BCryptPasswordEncoder(12);
    }
}