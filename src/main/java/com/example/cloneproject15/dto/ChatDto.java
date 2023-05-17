package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChatDto {

    //메시지 타입
    public enum MessageType {
        ENTER,
        TALK,
        LEAVE
    }

    private MessageType type;
    private String sender;
    private String userId;
    private String date;
    private String roomId;
    private String message;
    private String image;

    public ChatDto(Chat chat) {
        this.type = MessageType.TALK;
        this.sender = chat.getSender();
        this.userId = chat.getUser().getUserid();
        this.date = String.valueOf(chat.getCreatedDate());
        this.roomId = chat.getRoom().getRoomId();
        this.message = chat.getMessage();
        this.image = chat.getIamge_url();
    }
}