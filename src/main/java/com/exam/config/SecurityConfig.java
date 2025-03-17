package com.exam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // 개발과정 확인용 임시 파일
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 비활성화
        http.csrf((csrf) -> csrf.disable());

        // 요청 매핑값 허용 - 해당 페이지 접근 권한을 모든 사용자에게 부여
        http.authorizeHttpRequests(
                (auth) -> auth.requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
        );

        // 로그인 정보 설정
        http.formLogin(
                (formLogin) -> formLogin.loginPage("/login")
                        .loginProcessingUrl("/auth")
                        .usernameParameter("userid")
                        .passwordParameter("passwd")
                        .failureUrl("/login")
                        .defaultSuccessUrl("/", true)
        );

        // 로그아웃 정보 설정
        http.logout(
                (logout) -> logout.deleteCookies("remove")
                        .invalidateHttpSession(true)
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
        );

        return http.build();
    }
}
