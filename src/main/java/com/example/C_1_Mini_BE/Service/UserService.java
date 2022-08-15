package com.example.C_1_Mini_BE.Service;

import com.example.C_1_Mini_BE.Dto.Request.LoginRequestDto;
import com.example.C_1_Mini_BE.Dto.Request.TokenDto;
import com.example.C_1_Mini_BE.Dto.Request.UserRequestDto;
import com.example.C_1_Mini_BE.Dto.Response.ResponseDto;
import com.example.C_1_Mini_BE.Dto.Response.UserResponseDto;
import com.example.C_1_Mini_BE.Jwt.TokenProvider;
import com.example.C_1_Mini_BE.Model.User;
import com.example.C_1_Mini_BE.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor  // 초기화 되지 않은 필드의 생성자 생성
@Service  // 비즈니스 로직을 담은 클래스를 빈으로 등록시키기 위해 사용
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 시큐리티 기본제공 인터페이스로 비밀번호 암호화에 사용
    private final PasswordEncoder passwordEncoder;


    // 회원가입
    @Transactional  // 선언적 트랜잭션, 중간에 에러나면 없던 일로 처리해줌
    public ResponseDto<?> createUser(UserRequestDto requestDto) {
        if (null != isPresentUser(requestDto.getUsername())) {
            return ResponseDto.fail("DUPLICATED_USERNAME",
                    "중복된 아이디 입니다.");
        }
        if (null != isPresentNickname(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "중복된 닉네임 입니다.");
        }
        // 비밀번호 암호화를 위해 빌더패턴 사용
        User user = User.builder()
                .username(requestDto.getUsername())
                .nickname(requestDto.getNickname())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        userRepository.save(user);
        return ResponseDto.success(
                UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .createdAt(user.getCreatedAt())
                        .build()
        );
    }

    // 로그인
    @Transactional  // 선언적 트랜잭션, 중간에 에러나면 없던 일로 처리해줌
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        User user = isPresentUser(requestDto.getUsername());
        if (null == user) {
            return ResponseDto.fail("USERNAME_NOT_FOUND",
                    "유저정보가 없습니다.");
        }
        if (!user.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail("INVALID_PASSWORD", "비밀번호가 일치하지 않습니다.");
        }
        TokenDto tokenDto = tokenProvider.generateTokenDto(user);
        tokenToHeaders(tokenDto, response);
        return ResponseDto.success(
                UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .nickname(user.getNickname())
                        .createdAt(user.getCreatedAt())
                        .build()
        );
    }




    // 아이디 중복검사 및 찾기
    @Transactional // 선언적 트랜잭션, 중간에 에러나면 없던 일로 처리해줌
    public User isPresentUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElse(null);
    }
    // 닉네임 중복검사 및 찾기
    @Transactional // 선언적 트랜잭션, 중간에 에러나면 없던 일로 처리해줌
    public User isPresentNickname(String username) {
        Optional<User> optionalNickname = userRepository.findByNickname(username);
        return optionalNickname.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
