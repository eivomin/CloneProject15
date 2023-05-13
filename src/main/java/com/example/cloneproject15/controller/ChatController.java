package com.example.cloneproject15.controller;


import com.example.cloneproject15.dto.ChatMessage;
import com.example.cloneproject15.dto.ChatRoom;
import com.example.cloneproject15.dto.RoomDto;
import com.example.cloneproject15.entity.Message;
import com.example.cloneproject15.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatService.createRoom(name);
    }

    @GetMapping("/ex03")
    public List<RoomDto> getRooms() {
        List<RoomDto> rooms = chatService.getRooms();
        return rooms;
    }

    /* 게시글 전체 조회 + 검색 페이징 api */
    @GetMapping("/move/{room_id}")
    public ModelAndView moveList(@PathVariable String room_id){
        ModelAndView view = new ModelAndView();
        view.setViewName("detail");
        view.addObject("roomId", room_id);
        return view;
    }

    //방입장 API
    @MessageMapping("/chat/register")
    @SendTo("/topic/public")
    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    //메시지 전송 API
    @MessageMapping("/chat/send")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }
}