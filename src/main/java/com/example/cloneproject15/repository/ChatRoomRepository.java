package com.example.cloneproject15.repository;

import com.example.cloneproject15.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByRoomId(String roomId);

    //Optional<ChatRoom> findByHostAndGuest(String host, String guest);
    Optional<ChatRoom> findByHostAndRoomName(String host, String roomName);
    void deleteByRoomId(String roomId);

//    List<ChatRoom> findAllByHostIdOrGuestIdOrderByModifiedAtDesc(Long hostId, Long guestId);
}
