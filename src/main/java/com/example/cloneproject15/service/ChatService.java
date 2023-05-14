package com.example.cloneproject15.service;

import com.example.cloneproject15.dto.ChatDto;
import com.example.cloneproject15.dto.ResponseDto;
import com.example.cloneproject15.entity.ChatRoom;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.repository.ChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public ResponseDto createChatRoom(String receiver, String sender) {
        //이미 reciever와 sender로 생성된 채팅방이 있는지 확인
        Optional<ChatRoom> findChatRoom = validExistChatRoom(receiver, sender);
        //있으면 ChatRoom의 roomId 반환
        if(findChatRoom.isPresent())
            return ResponseDto.setSuccess("already has room and find Chatting Room Success!", findChatRoom.get().getRoomId());

        //없으면 receiver와 sender의 방을 생성해주고 roomId 반환
        ChatRoom newChatRoom = ChatRoom.of(receiver, sender);
        chatRoomRepository.save(newChatRoom);
        return ResponseDto.setSuccess("create ChatRoom success", newChatRoom.getRoomId());
    }

    public ChatDto enterChatRoom(ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {
        // 채팅방 찾기
        ChatRoom chatRoom = validExistChatRoom(chatDto.getRoomId());
        // 예외처리
        //반환 결과를 socket session에 사용자의 id로 저장
        headerAccessor.getSessionAttributes().put("nickname", chatDto.getSender());
        headerAccessor.getSessionAttributes().put("roomId", chatDto.getRoomId());

        chatDto.setMessage(chatDto.getSender() + "님 입장!! ο(=•ω＜=)ρ⌒☆");
        return chatDto;
    }

    public ChatDto disconnectChatRoom(SimpMessageHeaderAccessor headerAccessor) {
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String nickName = (String) headerAccessor.getSessionAttributes().get("nickname");

//        chatRoomRepository.deleteByRoomId(roomId);

        ChatDto chatDto = ChatDto.builder()
                .type(ChatDto.MessageType.LEAVE)
                .roomId(roomId)
                .sender(nickName)
                .message(nickName + "님 퇴장!! ヽ(*。>Д<)o゜")
                .build();

        return chatDto;
    }

    public Optional<ChatRoom> validExistChatRoom(String host, String guest) {
        return chatRoomRepository.findByHostAndGuest(host, guest);
    }

    public ChatRoom validExistChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                ()-> new NoSuchElementException("채팅방이 존재하지 않습니다.")
        );
    }
}