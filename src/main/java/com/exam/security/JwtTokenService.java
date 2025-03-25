package com.exam.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

// token 생성하는 기능
@Component  // @Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtTokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    //Spring Security에서 인증 결과인 Authentication 이용해서 token을 생성하는 메서드
    public String generateToken(Authentication authentication) {

        var scope = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(90, ChronoUnit.MINUTES)) // 90분 이후 만기
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    // JWT에서 memberId를 추출하는 메서드
    public String getMemberIdFromToken(String token) {
        // JWT 토큰을 디코딩
        Jwt decodedJwt = jwtDecoder.decode(token);

        // subject(일반적으로 사용자의 ID)가 memberId로 저장됨
        return decodedJwt.getSubject();  // subject 필드에서 memberId 반환
    }
}