package com.example.cloneproject15.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {
    private Long id;
    private String email;
    private String nickname;
    private String profile_image;

    public KakaoUserInfoDto(Long id, String nickname, String email, String profile_image) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profile_image = profile_image;
    }
}