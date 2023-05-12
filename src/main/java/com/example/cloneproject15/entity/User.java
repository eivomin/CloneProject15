package com.example.cloneproject15.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String userid;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = true)
    private Long kakaoid;

    @Column(nullable = true)
    private String email;

    @Column
    private String image_url;

    @Column
    private String category;

    public User(String userid, String password, String username, UserRoleEnum role, String image_url, String category) {
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.role = role;
        this.image_url = image_url;
        this.category = category;
    }

    public User(String userid, String password, String username, UserRoleEnum role, Long kakaoid, String email, String image_url, String category) {
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.role = role;
        this.kakaoid = kakaoid;
        this.email = email;
        this.image_url = image_url;
        this.category = category;
    }
}
