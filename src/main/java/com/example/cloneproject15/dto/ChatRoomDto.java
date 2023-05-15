package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Builder
@AllArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String roomName;
    private String host;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.host = chatRoom.getHost();

    }
}
