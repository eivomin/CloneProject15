package com.example.cloneproject15.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom room;

    //    @ManyToOne
//    private Member sender;
    private String sender;

    private String message;

/*    public static Chat of(ChatRequestDto dto, ChatRoom room, Member member){
        return Chat.builder()
                .room(room)
                .sender(member)
                .message(dto.getMessage())
                .build();
    }*/
}
