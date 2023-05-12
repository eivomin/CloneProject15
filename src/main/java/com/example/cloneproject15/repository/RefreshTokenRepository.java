package com.example.cloneproject15.repository;

import com.example.cloneproject15.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserid(String userid);
    void deleteByUserid(String userid);
}
