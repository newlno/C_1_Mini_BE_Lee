package com.example.C_1_Mini_BE.Controller;


import com.example.C_1_Mini_BE.Dto.Request.LoginRequestDto;
import com.example.C_1_Mini_BE.Dto.Request.UserRequestDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor  // 초기화 되지 않은 필드의 생성자 생성
@RestController  // 객체를 JSON 또는 XML 형식으로 HTTP응답에 반환
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/user/signup")
    public ResponseDto<?> signup(@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    // 로그인
    @PostMapping("/user/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto requestDto,
                                HttpServletResponse response // 이후 토큰 적용을 위한 선언
    ) {
        return userService.login(requestDto, response);
    }


}
