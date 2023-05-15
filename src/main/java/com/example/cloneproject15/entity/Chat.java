package com.example.cloneproject15.entity;

import com.example.cloneproject15.dto.ChatDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_Id")
    private ChatRoom room;

    //    @ManyToOne
//    private Member sender;
    private String sender;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

/*    public static Chat of(ChatRequestDto dto, ChatRoom room, Member member){
        return Chat.builder()
                .room(room)
                .sender(member)
                .message(dto.getMessage())
                .build();
    }*/
    public Chat (ChatDto chatDto) {
        this.sender = chatDto.getSender();
        this.message = chatDto.getMessage();
        //this.room = chatDto.getRoomId();
    }
}
