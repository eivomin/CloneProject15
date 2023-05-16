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

    @Column(nullable = true)
    private String iamge_url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Chat (ChatDto chatDto, String image_url, ChatRoom room, User user) {
        this.sender = chatDto.getSender();
        this.message = chatDto.getMessage();
        this.iamge_url = image_url;
        this.room = room;
        this.user = user;
    }


}
