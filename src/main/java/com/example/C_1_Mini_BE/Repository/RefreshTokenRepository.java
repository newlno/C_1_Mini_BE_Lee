package com.example.C_1_Mini_BE.Repository;

import com.example.C_1_Mini_BE.Model.RefreshToken;
import com.example.C_1_Mini_BE.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByUser(User user);
}
