package com.example.cloneproject15.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    private String userid;
    private String username;
    private String password;
    private String birthday;

    public UserRequestDto(String userid, String username, String password, String birthday) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
    }
}
