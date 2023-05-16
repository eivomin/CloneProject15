package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.ChatRoom;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto {
    private String roomId;
    private String roomName;
    private String host;

    public ChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.host = chatRoom.getHost();
    }

    public ChatRoomDto(String roomId, String roomName, String host) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.host = host;
    }

}
