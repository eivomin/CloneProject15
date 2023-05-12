package com.example.cloneproject15.dto;

import com.example.cloneproject15.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String userid;
    private String username;
    private String category;
    private String image_url;

    public UserResponseDto(String userid, String username, String category, String image_url) {
        this.userid = userid;
        this.username = username;
        this.category = category;
        this.image_url = image_url;
    }

    public UserResponseDto(User user){
        this.userid = user.getUserid();
        this.username = user.getUsername();
        this.category = user.getCategory();
        this.image_url = user.getImage_url();
    }
}