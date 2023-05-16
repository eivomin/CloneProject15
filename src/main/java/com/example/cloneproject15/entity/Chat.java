package com.example.cloneproject15.entity;

import com.example.cloneproject15.dto.ChatDto;
import com.example.cloneproject15.dto.ChatRoomDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Chat extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_Id")
    private ChatRoom room;

    private String sender;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Chat (ChatDto chatDto, ChatRoom room, User user) {
        this.sender = chatDto.getSender();
        this.message = chatDto.getMessage();
        this.room = room;
        this.user = user;
    }


}
