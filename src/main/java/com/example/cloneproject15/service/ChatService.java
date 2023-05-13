package com.example.cloneproject15.service;

import com.example.cloneproject15.dto.ChatRoom;
import com.example.cloneproject15.dto.RoomDto;
import com.example.cloneproject15.entity.Room;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms;
    private final ChatRoomRepository chatRoomRespository;

    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    @Transactional(readOnly = true)
    // 게시글 전체 조회
    public List<RoomDto> getRooms(){
        List<Room> roomList = chatRoomRespository.findAll();
        return roomList.stream().map(RoomDto::new).collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        User user = new User();
        chatRoomRespository.save(new Room(randomId, name));

        return chatRoom;
    }

//    public ChatRoom createRoom(String name, User user) {
//        String randomId = UUID.randomUUID().toString();
//        ChatRoom chatRoom = ChatRoom.builder()
//                .roomId(randomId)
//                .name(name)
//                .username(user.getUsername())
//                .build();
//        chatRooms.put(randomId, chatRoom);
//
//        chatRoomRespository.save(new Room(randomId, name, user));
//
//        return chatRoom;
//    }


}