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
    void findChatRoomTest() {
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