package com.example.cloneproject15.controller;


import com.example.cloneproject15.dto.ChatDto;
import com.example.cloneproject15.dto.ChatRoomDto;
import com.example.cloneproject15.dto.ResponseDto;
import com.example.cloneproject15.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
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

    @Operation(summary = "채팅방 생성 API" , description = "새로운 채팅방 생성")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "채팅방 생성 완료" )})
    @PostMapping("/chat")
    public ResponseDto createChatRoom(@RequestBody ChatRoomDto chatRoomDto) {
        // @Param sender should be replaced to UserDetails.getMember();
        return chatService.createChatRoom(chatRoomDto.getRoomName(), chatRoomDto.getHost());
        // createChatRoom의 결과인 roomId와 type : ENTER을 저장한 chatDto에 넣어줘야함
    }

    @MessageMapping("/chat/enter")
    @SendTo("/sub/chat/room")
    public void enterChatRoom(ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(500); // simulated delay
        ChatDto newchatdto = chatService.enterChatRoom(chatDto, headerAccessor);
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), newchatdto);
    }

    @MessageMapping("/chat/send")
    @SendTo("/sub/chat/room")
    public void sendChatRoom(ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(500); // simulated delay
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        ChatDto chatDto = chatService.disconnectChatRoom(headerAccessor);
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
    }

//    채팅방 목록 조회
    @GetMapping("/room")
    public List<ChatRoomDto> showRoomList() {
        return chatService.showRoomList();
    }

}