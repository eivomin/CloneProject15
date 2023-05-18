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
    private Long headCount;
    private String profile_image;

    public ChatRoomDto(ChatRoom chatRoom, String profile_image) {
        this.roomId = chatRoom.getRoomId();
        this.roomName = chatRoom.getRoomName();
        this.host = chatRoom.getHost();
        this.profile_image = profile_image;
    }

    public ChatRoomDto(String roomId, String roomName, String host) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.host = host;
    }

}
