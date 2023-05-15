package com.example.cloneproject15.entity;

import com.example.cloneproject15.dto.UserRequestDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends TimeEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Pattern(regexp = "[a-zA-Z0-9]{4,12}$", message="아이디는 알파벳 대문자, 소문자, 숫자 포함 4~12자리여야 합니다.")
    @Column(nullable = false)
    private String userid;

    @Column(nullable = false)
    private String password;

    @Pattern(regexp = "[가-힣]{2,10}$", message="이름은 한글 2~10자리여야 합니다.")
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Column(nullable = true)
    private Long kakaoid;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String image_url;

    @Column(nullable = false)
    private String birthday;

    public User(String userid, String password, String username, UserRoleEnum role, String image_url, String birthday) {
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.role = role;
        this.image_url = image_url;
        this.birthday = birthday;
    }

    public User(String userid, String password, String username, UserRoleEnum role, Long kakaoid, String email, String image_url, String birthday) {
        this.userid = userid;
        this.password = password;
        this.username = username;
        this.role = role;
        this.kakaoid = kakaoid;
        this.email = email;
        this.image_url = image_url;
        this.birthday = birthday;
    }

    public void update(UserRequestDto userRequestDto){
        this.password = userRequestDto.getPassword();
        this.username = userRequestDto.getUsername();
//        this.email = userRequestDto.getEmail();
//        this.image_url = userRequestDto.getImage_url();
        this.birthday = userRequestDto.getBirthday();
    }

    public User kakaoIdUpdate(Long kakaoid) {
        this.kakaoid = kakaoid;
        return this;
    }

}
