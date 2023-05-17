package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.Chat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EnterUserDto {

    private String sender;
    private String userId;
    private String roomId;
    private String image_url;
    private List<ChatDto> chatList;

    public EnterUserDto(String sender, String userId, String roomId, String image_url, List<ChatDto> chatDtoList) {
        this.sender = sender;
        this.userId = userId;
        this.roomId = roomId;
        this.image_url = image_url;
        this.chatList = chatDtoList;
    }
}

