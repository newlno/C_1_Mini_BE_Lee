package com.example.C_1_Mini_BE.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder  // 빌더패턴 사용하기 위한 어노테이션
@Getter  // 멤버변수 값 호출
@NoArgsConstructor  // 기본 생성자를 생성
@AllArgsConstructor //전체 변수를 생성하는 생성자
public class CommentResponseDto {
    private Long id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
