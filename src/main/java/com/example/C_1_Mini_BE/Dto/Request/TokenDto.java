package com.example.C_1_Mini_BE.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
  private String grantType;
  private String accessToken;
  private String refreshToken;
  private Long accessTokenExpiresIn;
}
