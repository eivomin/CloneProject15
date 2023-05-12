package com.example.cloneproject15.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "user_id")
    private String userId;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String profileImageUrl;

    @Column
    private ProviderType providerType;

}
