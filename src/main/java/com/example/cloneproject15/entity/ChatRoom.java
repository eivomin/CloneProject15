package com.example.cloneproject15.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "TB_CHATROOM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String roomName;

    @OneToMany(mappedBy = "room", cascade = CascadeType.REMOVE)
    private List<Chat> chatLists = new ArrayList<>();

    @Column(nullable = false)
    private String host;

    @Column
    private String userid;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Long headCount;

    public ChatRoom(String roomName, String host, String userid) {
        this.roomId = UUID.randomUUID().toString();
        this.roomName = roomName;
        this.host = host;
        this.userid = userid;
        this.headCount = 0L;
    }

    public void updateCount(Long headCount) {
        this.headCount = headCount;
    }

}