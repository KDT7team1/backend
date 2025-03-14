package com.exam.member;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())   // csrf 보호 비활성화 (API 요청 시 필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/member/register", "/member/login").permitAll()   // 회원가입, 로그인 허용
                        .anyRequest().authenticated()   // 그 외 요청은 인증 필요
                );
        return http.build();
    }

}
