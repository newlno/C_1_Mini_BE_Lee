package com.example.C_1_Mini_BE.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter  // 멤버변수 값 호출
@AllArgsConstructor //전체 변수를 생성하는 생성자
public class ResponseDto<T> {
  private boolean success;
  private T data;
  private Error error;

  public static <T> ResponseDto<T> success(T data) {
    return new ResponseDto<>(true, data, null);
  }

  public static <T> ResponseDto<T> fail(String code, String message) {
    return new ResponseDto<>(false, null, new Error(code, message));
  }

  @Getter
  @AllArgsConstructor
  static class Error {
    private String code;
    private String message;
  }

}
