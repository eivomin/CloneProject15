package com.example.cloneproject15.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EnterUserDto {
    private String sender;
    private String userId;
    private String roomId;
    private String image_url;

    public EnterUserDto(String sender, String userId, String roomId, String image_url) {
        this.sender = sender;
        this.userId = userId;
        this.roomId = roomId;
        this.image_url = image_url;
    }
}
