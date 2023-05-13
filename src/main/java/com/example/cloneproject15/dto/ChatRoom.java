package com.example.cloneproject15.dto;

import com.example.cloneproject15.dto.ChatMessage;
import com.example.cloneproject15.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    private String username;
    //private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name, String username) {
        this.roomId = roomId;
        this.name = name;
        this.username = username;
    }

//    public void handlerActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
//        if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
//            sessions.add(session);
//            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
//        }
//        sendMessage(chatMessage, chatService);
//
//    }
//
//    private <T> void sendMessage(T message, ChatService chatService) {
//        sessions.parallelStream()
//                .forEach(session -> chatService.sendMessage(session, message));
//    }
}
