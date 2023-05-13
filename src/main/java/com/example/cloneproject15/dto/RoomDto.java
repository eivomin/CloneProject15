package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.Room;
import lombok.Getter;

@Getter
public class RoomDto {
    private Long id;
    private String roomId;
    private String name;

    public RoomDto(Room room){
        this.id = room.getId();
        this.roomId = room.getRoomId();
        this.name = room.getName();
    }
}
