package com.example.cloneproject15.controller;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.cloneproject15.dto.ChatDto;
import com.example.cloneproject15.dto.ChatRoomDto;
import com.example.cloneproject15.dto.EnterUserDto;
import com.example.cloneproject15.dto.ResponseDto;
import com.example.cloneproject15.entity.Chat;
import com.example.cloneproject15.entity.ChatRoom;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.repository.ChatRepository;
import com.example.cloneproject15.repository.ChatRoomRepository;
import com.example.cloneproject15.repository.UserRepository;
import com.example.cloneproject15.security.UserDetailsImpl;
import com.example.cloneproject15.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;

//import static com.example.cloneproject15.service.UserService.S3_BUCKET_PREFIX;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "ChatController", description = "채팅 관련 Controller")
public class ChatController {

    //image url 변환//////////////////
    private static final String S3_BUCKET_PREFIX = "S3";

    @Value("chattingroom")
    private String bucketName;
    private final AmazonS3 amazonS3;
    ///////////////////////////////
    
    private final ChatService chatService;
    private final SimpMessagingTemplate msgOperation;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Operation(summary = "채팅방 생성 API" , description = "새로운 채팅방 생성")
    @ApiResponses(value ={@ApiResponse(responseCode= "200", description = "채팅방 생성 완료" )})
    @PostMapping("/chat")
    public ResponseDto createChatRoom(@RequestBody ChatRoomDto chatRoomDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        chatRoomDto.setHost(userDetails.getUser().getUsername());
        return chatService.createChatRoom(chatRoomDto.getRoomName(), chatRoomDto.getHost());
    }

    @GetMapping("/chat/{roomId}")
    public EnterUserDto findChatRoom(@PathVariable String roomId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("userDetails = " + userDetails.getUser().getUsername());
        return chatService.findRoom(roomId, userDetails.getUser().getUsername());
    }

    @MessageMapping("/chat/enter")
    @SendTo("/sub/chat/room")
    public void enterChatRoom(@RequestBody ChatDto chatDto, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(500); // simulated delay
        ChatDto newchatdto = chatService.enterChatRoom(chatDto, headerAccessor);
//        User user = userNameCheck(chatDto.getSender());
//        ChatRoom room = roomIdCheck(chatDto.getRoomId());
//        user.enterRoom(room);  // --->transactional?
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), newchatdto);
    }

    @MessageMapping("/chat/send")
    @SendTo("/sub/chat/room")
    @Transactional
    public void sendChatRoom(@RequestBody ChatDto chatDto, @RequestParam(value = "image", required = false) MultipartFile image, SimpMessageHeaderAccessor headerAccessor) throws Exception {
        Thread.sleep(500); // simulated delay
        ChatRoom room = roomIdCheck(chatDto.getRoomId());
        User user = userNameCheck(chatDto.getSender());
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
        
        //image -> url로 변경 
        //service단으로 옮길 예정
        //새로운 파일명 부여를 위한 현재 시간 알아내기
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        String image_url = null;

        if(image != null){
            //새로 부여한 이미지 명
            String newFileName = "image"+hour+minute+second+millis;
            String fileExtension = '.'+image.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1");
            String imageName = S3_BUCKET_PREFIX + newFileName + fileExtension;

            //메타데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(image.getContentType());
            objectMetadata.setContentLength(image.getSize());

            InputStream inputStream = image.getInputStream();

            amazonS3.putObject(new PutObjectRequest(bucketName, imageName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            image_url = amazonS3.getUrl(bucketName, imageName).toString();
        }

       Chat chat = new Chat(chatDto, image_url, room, user);

       chatRepository.save(chat);
    }

    @EventListener
    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        ChatDto chatDto = chatService.disconnectChatRoom(headerAccessor);
        msgOperation.convertAndSend("/sub/chat/room" + chatDto.getRoomId(), chatDto);
    }

    @GetMapping("/room")
    public List<ChatRoomDto> showRoomList() {
        return chatService.showRoomList();
    }

    //방의 존재유무 확인
    public ChatRoom roomIdCheck(String roomId) {
        return chatRoomRepository.findByRoomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 채팅방입니다.")
        );
    }

    //유저 확인
    public User userNameCheck(String userName) {
        return userRepository.findByUsername(userName).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 유저입니다.")
        );
    }

}