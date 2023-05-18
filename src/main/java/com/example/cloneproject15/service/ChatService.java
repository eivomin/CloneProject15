package com.example.cloneproject15.service;

import com.example.cloneproject15.dto.ChatDto;
import com.example.cloneproject15.dto.ChatRoomDto;
import com.example.cloneproject15.dto.EnterUserDto;
import com.example.cloneproject15.dto.ResponseDto;
import com.example.cloneproject15.entity.Chat;
import com.example.cloneproject15.entity.ChatRoom;
import com.example.cloneproject15.entity.MessageType;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.repository.ChatRepository;
import com.example.cloneproject15.repository.ChatRoomRepository;
import com.example.cloneproject15.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public ResponseDto createChatRoom(String roomName, String host, User user) {
        //이미 reciever와 sender로 생성된 채팅방이 있는지 확인
        Optional<ChatRoom> findChatRoom = validExistChatRoom(host, roomName);
        //있으면 ChatRoom의 roomId 반환
        if(findChatRoom.isPresent())
            return ResponseDto.setSuccess("already has room and find Chatting Room Success!", findChatRoom.get().getRoomId());

        //없으면 receiver와 sender의 방을 생성해주고 roomId 반환
        //ChatRoom newChatRoom = ChatRoom.of(receiver, sender);
        //String roomId, String roomName, String host, String guest
        ChatRoom newChatRoom = new ChatRoom(roomName, host, user.getUserid());
        chatRoomRepository.save(newChatRoom);
        return ResponseDto.setSuccess("create ChatRoom success", newChatRoom.getRoomId());
    }

    public ChatDto enterChatRoom(ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) {
        // 채팅방 찾기
        ChatRoom chatRoom = validExistChatRoom(chatDto.getRoomId());
        // 예외처리
        //반환 결과를 socket session에 사용자의 id로 저장
        headerAccessor.getSessionAttributes().put("userId", chatDto.getUserId());
        headerAccessor.getSessionAttributes().put("roomId", chatDto.getRoomId());
        headerAccessor.getSessionAttributes().put("nickName", chatDto.getSender());


        //User user = userNameCheck(chatDto.getSender());
        User user = userIDCheck(chatDto.getUserId());
        ChatRoom room = roomIdCheck(chatDto.getRoomId());
        user.enterRoom(room);

        chatDto.setMessage(chatDto.getSender() + "님 입장!! ο(=•ω＜=)ρ⌒☆");

        Long headCount = userRepository.countAllByRoom_Id(chatRoom.getId());
        chatRoom.updateCount(headCount);
        return chatDto;
    }

    public ChatDto disconnectChatRoom(SimpMessageHeaderAccessor headerAccessor) {
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        String nickName = (String) headerAccessor.getSessionAttributes().get("nickName");
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        //User user = userNameCheck(nickName);
        User user = userIDCheck(userId);
        ChatRoom room = roomIdCheck(roomId);
        user.exitRoom(room);

//        chatRoomRepository.deleteByRoomId(roomId);

        ChatDto chatDto = ChatDto.builder()
                .type(MessageType.LEAVE)
                .roomId(roomId)
                .sender(nickName)
                .userId(userId)
                .message(nickName + "님 퇴장!! ヽ(*。>Д<)o゜")
                .build();

        //LEAVE할때 headcount가 0이면 방 삭제
        Long headCount = userRepository.countAllByRoom_Id(room.getId());
        room.updateCount(headCount);
        if(headCount == 0){
            chatRoomRepository.deleteByRoomId(roomId);
        }

        return chatDto;
    }

    public Optional<ChatRoom> validExistChatRoom(String host, String roomName) {
        //return chatRoomRepository.findByHostAndGuest(host, guest);
        return chatRoomRepository.findByHostAndRoomName(host, roomName);
    }

    public ChatRoom validExistChatRoom(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                ()-> new NoSuchElementException("채팅방이 존재하지 않습니다.")
        );
    }

    public List<ChatRoomDto> showRoomList() {
        List<ChatRoom>chatRoomList = chatRoomRepository.findAll();
        List<ChatRoomDto> chatRoomDtoList = new ArrayList<>();
        for (ChatRoom chatRoom : chatRoomList) {
            Optional<User> findUser = userRepository.findByUserid(chatRoom.getUserid());
            String profile_image = findUser.get().getProfile_image();
            ChatRoomDto chatRoomDto = new ChatRoomDto(chatRoom, profile_image);
            chatRoomDtoList.add(chatRoomDto);
        }
        return chatRoomDtoList;
    }

    public ChatRoom roomIdCheck(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
    }

    public User userNameCheck(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

    //유저 확인 (추가)
    public User userIDCheck(String userId) {
        return userRepository.findByUserid(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

    public EnterUserDto findRoom(String roomId, String userName) {
        ChatRoom chatRoom = roomIdCheck(roomId);
        User user = userNameCheck(userName);
        List<Chat> chatList = chatRepository.findAllByRoom_IdOrderByCreatedDateAsc(chatRoom.getId());
        List<ChatDto> chatDtoList = new ArrayList<>();
        for (Chat chat : chatList) {
            ChatDto chatDto = new ChatDto(chat);
            chatDtoList.add(chatDto);
        }
        return new EnterUserDto(userName, user.getUserid(), chatRoom.getRoomId(), user.getProfile_image(), chatDtoList);
    }
}