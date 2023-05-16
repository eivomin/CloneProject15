package com.example.cloneproject15.service;

import com.example.cloneproject15.dto.StatusResponseDto;
import com.example.cloneproject15.dto.TokenDto;
import com.example.cloneproject15.dto.UserRequestDto;
import com.example.cloneproject15.dto.UserResponseDto;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.entity.UserRoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    HttpServletResponse response;
    @Autowired
    HttpServletRequest request;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("user1233", "유민", "User123", "1996-11-19");

        //when
        StatusResponseDto statusResponseDto = userService.signup(userRequestDto, null);

        //then
        assertEquals(statusResponseDto.getMeassage(), "회원가입 성공");
    }

    @Test
    public void 로그인() throws Exception {
        //given
        UserRequestDto userRequestDto = new UserRequestDto("user1233", "유민", "User123", "1996-11-19");

        //when
        StatusResponseDto statusResponseDto = userService.login(userRequestDto, response);

        //then
        assertEquals(statusResponseDto.getMeassage(), "로그인 성공");
    }

//    @Test
//    public void 로그아웃() throws Exception {
//        //given
//        User user = new User("user1233", "User123", "유민", UserRoleEnum.USER, "image_url", "1996-11-19");
//
//        //when
//        String token = "";
//
//        StatusResponseDto statusResponseDto = userService.logout(user, request);
//
//        //then
//        assertEquals(statusResponseDto.getMeassage(), "로그아웃 성공");
//    }

}
