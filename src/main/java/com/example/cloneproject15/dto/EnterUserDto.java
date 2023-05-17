package com.example.cloneproject15.dto;

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
    private String profile_image;
    private List<ChatDto> chatList;

    public EnterUserDto(String sender, String userId, String roomId, String profile_image, List<ChatDto> chatDtoList) {
        this.sender = sender;
        this.userId = userId;
        this.roomId = roomId;
        this.profile_image = profile_image;
        this.chatList = chatDtoList;
    }
}

