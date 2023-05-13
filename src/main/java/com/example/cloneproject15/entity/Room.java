package com.example.cloneproject15.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Room {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private String name;

    public Room(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
}