package com.example.cloneproject15.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;

    private String roomName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    List<Chat> chatLists = new ArrayList<>();

    @Column(name = "host")
    private String host;

    @Column(name = "guest")
    private String guest;

    public static ChatRoom of(String host, String guest) {
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(host + "님과의 대화 ο(=•ω＜=)ρ⌒☆")
                .host(host)
                .guest(guest)
                .build();
    }
    //private String lastChat;

/*    public static ChatRoom of(Member host, Member guest) {
        return ChatRoom.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(host.getNickname() + "님과의 대화 ο(=•ω＜=)ρ⌒☆")
                .host(host.getNickname())
                .guest(guest.getNickname())
                .build();
    }*/

//    public boolean isHost(Member member) {
//        return getHost().getId().equals(member.getId());
//    }
}