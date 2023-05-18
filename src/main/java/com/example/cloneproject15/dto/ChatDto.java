package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.Chat;
import com.example.cloneproject15.entity.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatDto {

    private MessageType type;
    private String sender;
    private String userId;
    private String roomId;
    private String date;
    private String message;
    private String profile_image;
    private String image;

    public ChatDto(Chat chat) {
        this.type = chat.getType();
        this.sender = chat.getSender();
        this.userId = chat.getUser().getUserid();
        this.roomId = chat.getRoom().getRoomId();
        this.message = chat.getMessage();
        this.profile_image = chat.getProfile_image();
    }
}