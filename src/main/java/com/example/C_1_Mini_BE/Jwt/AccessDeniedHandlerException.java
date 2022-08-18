package com.example.C_1_Mini_BE.Jwt;

import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerException implements AccessDeniedHandler {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {
    String result = objectMapper.writeValueAsString(ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다."));
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    response.getWriter().write(result);
  }
}
