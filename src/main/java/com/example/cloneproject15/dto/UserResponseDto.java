package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String userid;
    private String username;
    private String birthday;
    private String image_url;

    public UserResponseDto(String userid, String username, String birthday, String image_url) {
        this.userid = userid;
        this.username = username;
        this.birthday = birthday;
        this.image_url = image_url;
    }

    public UserResponseDto(User user){
        this.userid = user.getUserid();
        this.username = user.getUsername();
        this.birthday = user.getBirthday();
        this.image_url = user.getImage_url();
    }
}