package com.example.C_1_Mini_BE.Jwt;


import com.example.C_1_Mini_BE.Dto.Request.TokenDto;
import com.example.C_1_Mini_BE.Model.User;
import com.example.C_1_Mini_BE.Model.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

  private static final String BEARER_PREFIX = "Bearer ";

  private final Key key;

  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;


  public TokenProvider(@Value("${jwt.secret}") String secretKey) {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenDto generateTokenDto(User user) {
    long now = (new Date().getTime());
    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
            // 여기서 맴버의 닉네임을 넣어줌
            .setSubject(user.getUsername())
            .signWith(key, SignatureAlgorithm.HS256)
            .setExpiration(accessTokenExpiresIn)
            .compact();

    return TokenDto.builder()
            .grantType(BEARER_PREFIX)
            .accessToken(accessToken)
            .build();
  }

  public boolean validateToken(String token) {
    System.out.println(token);
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      // 여기서 토큰 검증함
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
    } catch (ExpiredJwtException e) {
      log.info("Expired JWT token, 만료된 JWT token 입니다.");
    } catch (UnsupportedJwtException e) {
      log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
    } catch (IllegalArgumentException e) {
      log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
    }
    return false;
  }
}

