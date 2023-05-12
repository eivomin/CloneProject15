package com.example.cloneproject15.controller;


import com.example.cloneproject15.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Tag(name = "ChatController", description = "채팅 Controller")
//@RequestMapping("/chat")
public class ChatController {
//    private final ChatService chatService;
//
//    @PostMapping
//    public ChatRoom createRoom(@RequestBody String name) {
//        return chatService.createRoom(name);
//    }
//
//    @GetMapping
//    public List<ChatRoom> findAllRoom() {
//        return chatService.findAllRoom();
//    }
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Operation(summary = "메시지송신 API" , description = "받는 메시지")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "회원 가입 완료" )})
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public Message receiveMessage(@Payload Message message){
        return message;
    }

    @Operation(summary = "rec 메시지 API" , description = "새로운 유저 가입")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "회원 가입 완료" )})
    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message);
        System.out.println(message.toString());
        return message;
    }
}