package com.example.cloneproject15.repository;

import com.example.cloneproject15.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
