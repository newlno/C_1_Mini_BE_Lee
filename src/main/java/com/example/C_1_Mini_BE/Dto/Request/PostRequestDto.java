package com.example.C_1_Mini_BE.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter  // 멤버변수 값 호출
@NoArgsConstructor  // 기본 생성자를 생성
@AllArgsConstructor //전체 변수를 생성하는 생성자
public class PostRequestDto {
    private String title;
    private String content;
    private String category;
    private String imgUrl;
}
