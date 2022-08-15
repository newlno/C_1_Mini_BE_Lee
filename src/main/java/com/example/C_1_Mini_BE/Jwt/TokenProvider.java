package com.example.C_1_Mini_BE.Jwt;


import com.example.C_1_Mini_BE.Dto.Request.TokenDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Model.Authority;
import com.example.C_1_Mini_BE.Model.RefreshToken;
import com.example.C_1_Mini_BE.Model.User;
import com.example.C_1_Mini_BE.Model.UserDetailsImpl;
import com.example.C_1_Mini_BE.Repository.RefreshTokenRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24;            //1일
  private static final long REFRESH_TOKEN_EXPRIRE_TIME = 1000 * 60 * 60 * 24 * 7;     //7일

  private final Key key;

  private final RefreshTokenRepository refreshTokenRepository;

  public TokenProvider(@Value("${jwt.secret}") String secretKey,
                       RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public TokenDto generateTokenDto(User user) {
    long now = (new Date().getTime());

    Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
    String accessToken = Jwts.builder()
            // 여기서 맴버의 닉네임을 넣어줌
            .setSubject(user.getNickname())
            .claim(AUTHORITIES_KEY, Authority.ROLE_MEMBER.toString())
            .setExpiration(accessTokenExpiresIn)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    String refreshToken = Jwts.builder()
            .setSubject(String.valueOf(user.getId())) // 추가한 부분
            .setExpiration(new Date(now + REFRESH_TOKEN_EXPRIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();

    RefreshToken refreshTokenObject = RefreshToken.builder()
            .id(user.getId())
            .user(user)
            .value(refreshToken)
            .build();

    refreshTokenRepository.save(refreshTokenObject);

    return TokenDto.builder()
            .grantType(BEARER_PREFIX)
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
            .refreshToken(refreshToken)
            .build();

  }


  public User getUserFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || AnonymousAuthenticationToken.class.
            isAssignableFrom(authentication.getClass())) {
      return null;
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getUser();
  }


  public boolean validateToken(String token) {
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


  @Transactional(readOnly = true)
  public RefreshToken isPresentRefreshToken(User user) {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUser(user);
    return optionalRefreshToken.orElse(null);
  }

  @Transactional
  public ResponseDto<?> deleteRefreshToken(User user) {
    RefreshToken refreshToken = isPresentRefreshToken(user);
    if (null == refreshToken) {
      return ResponseDto.fail("TOKEN_NOT_FOUND", "존재하지 않는 Token 입니다.");
    }

    refreshTokenRepository.delete(refreshToken);
    return ResponseDto.success("success");
  }
}

