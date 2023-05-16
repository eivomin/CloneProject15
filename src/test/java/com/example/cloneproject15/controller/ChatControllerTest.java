package com.example.cloneproject15.controller;

import com.example.cloneproject15.dto.ChatRoomDto;
import com.example.cloneproject15.dto.EnterUserDto;
import com.example.cloneproject15.entity.ChatRoom;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.entity.UserRoleEnum;
import com.example.cloneproject15.security.UserDetailsImpl;
import com.example.cloneproject15.service.ChatService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChatControllerTest {

    @Test
    @DisplayName("채팅방 생성 테스트")
    void createChatRoom() {
        User user = new User("user1", "Testtest123", "김수박", UserRoleEnum.USER, null, "null", null, "930607" );

        String roomId = UUID.randomUUID().toString();
        String host = user.getUsername();
        ChatRoomDto chatRoomDto = new ChatRoomDto(roomId, "1번 테스트 방", null);
        chatRoomDto.setHost(host);

        ChatRoom chatRoom = new ChatRoom(chatRoomDto.getRoomName(), chatRoomDto.getHost());

        assertEquals(chatRoom.getRoomName(), "1번 테스트 방");
    }

    @Test
    @DisplayName("채팅방 조회 API")
    void findChatRoomTest() {
//        // Mock 객체
//        ChatService chatService = Mockito.mock(ChatService.class);
//
//        //String sender, String userId, String roomId, String image_url
//        EnterUserDto testEnterUserDto = new EnterUserDto("김수박", "user1", "1a4a80c3-cd10-45a9-9f5d-e481857ea2a3", "https://chattingroom.s3.ap-northeast-2.amazonaws.com/S3image182630819.webp");
//        Mockito.when(chatService.findRoom(Mockito.anyString(), Mockito.anyString()))
//                .thenReturn(testEnterUserDto);
//
//        String roomId = "1a4a80c3-cd10-45a9-9f5d-e481857ea2a3";
//        UserDetailsImpl userDetails = new UserDetailsImpl(new User("user1", "Testtest123", "김수박", UserRoleEnum.USER, null, "null", null, "1993-06-07" ));
//        testEnterUserDto
    }

    @Test
    void enterChatRoom() {
    }

    @Test
    void sendChatRoom() {
    }

    @Test
    void webSocketDisconnectListener() {
    }

    @Test
    void showRoomList() {
    }

    @Test
    void roomIdCheck() {
    }

    @Test
    void userNameCheck() {
    }
}