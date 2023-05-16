package com.example.cloneproject15.controller;


import com.example.cloneproject15.dto.ChatDto;
import com.example.cloneproject15.dto.ChatRoomDto;
import com.example.cloneproject15.dto.ResponseDto;
import com.example.cloneproject15.entity.Chat;
import com.example.cloneproject15.entity.ChatRoom;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.repository.ChatRepository;
import com.example.cloneproject15.repository.ChatRoomRepository;
import com.example.cloneproject15.repository.UserRepository;
import com.example.cloneproject15.security.UserDetailsImpl;
import com.example.cloneproject15.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "ChatController", description = "채팅 관련 Controller")
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate msgOperation;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Operation(summary = "채팅방 생성 API" , description = "새로운 채팅방 생성")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "채팅방 생성 완료" )})
    @PostMapping("/chat")
    public ResponseDto createChatRoom(@RequestBody ChatRoomDto chatRoomDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomDto.setHost(userDetails.getUsername());
        return chatService.createChatRoom(chatRoomDto.getRoomName(), chatRoomDto.getHost());
    }

    @MessageMapping("/chat/enter")
    @SendTo("/sub/chat/room")
    public ResponseDto enterChatRoom(@RequestBody ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(500); // simulated delay
        ChatDto newchatdto = chatService.enterChatRoom(chatDto, headerAccessor);
//        User user = userNameCheck(chatDto.getSender());
//        ChatRoom room = roomIdCheck(chatDto.getRoomId());
//        user.enterRoom(room);  // --->transactional?
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), newchatdto);
        return ResponseDto.setSuccess("enter room success", chatDto.getRoomId());
    }

    @MessageMapping("/chat/send")
    @SendTo("/sub/chat/room")
    @Transactional
    public String sendChatRoom(@RequestBody ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(500); // simulated delay
        ChatRoom room = roomIdCheck(chatDto.getRoomId());
        User user = userNameCheck(chatDto.getSender());
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
       Chat chat = new Chat(chatDto, room, user);
       chatRepository.save(chat);
       return "sendChatRoom";
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        ChatDto chatDto = chatService.disconnectChatRoom(headerAccessor);
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
    }

    @GetMapping("/room")
    public List<ChatRoomDto> showRoomList() {
        return chatService.showRoomList();
    }

    //방의 존재유무 확인
    public ChatRoom roomIdCheck(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
    }

    //유저 확인
    public User userNameCheck(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

}