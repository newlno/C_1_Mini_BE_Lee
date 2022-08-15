package com.example.C_1_Mini_BE.Repository;

import com.example.C_1_Mini_BE.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{

    // 아이디, 닉네임 중복검사
    Optional<User> findByUsername(String username);
    Optional<User> findByNickname(String nickname);


}
