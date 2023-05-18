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
    private String profile_image;
    private String comment;

    public UserResponseDto(User user){
        this.userid = user.getUserid();
        this.username = user.getUsername();
        this.birthday = user.getBirthday();
        this.profile_image = user.getProfile_image();
        this.comment = user.getComment();
    }
}