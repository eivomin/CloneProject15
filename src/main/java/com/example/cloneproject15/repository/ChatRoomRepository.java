package com.example.cloneproject15.repository;

import com.example.cloneproject15.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<Room, Long> {
}
