package com.example.cloneproject15.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.cloneproject15.config.SentrySupport;
import com.example.cloneproject15.dto.StatusResponseDto;
import com.example.cloneproject15.dto.TokenDto;
import com.example.cloneproject15.dto.UserRequestDto;
import com.example.cloneproject15.dto.UserResponseDto;
import com.example.cloneproject15.entity.RefreshToken;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.entity.UserRoleEnum;
import com.example.cloneproject15.exception.ApiException;
import com.example.cloneproject15.exception.ExceptionEnum;
import com.example.cloneproject15.jwt.JwtUtil;
import com.example.cloneproject15.repository.RefreshTokenRepository;
import com.example.cloneproject15.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SentrySupport sentrySupport;
    private static final String S3_BUCKET_PREFIX = "S3";

    @Value("chattingroom")
    private String bucketName;
    private final AmazonS3 amazonS3;

    public StatusResponseDto signup(UserRequestDto requestDto, MultipartFile image) throws IOException {

        String userid = requestDto.getUserid();
        String username = requestDto.getUsername();
        String birthday = requestDto.getBirthday();

        if(!requestDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")){ // 비밀번호 정규식 체크
            sentrySupport.logSimpleMessage((ExceptionEnum.PASSWAORD_REGEX).getMessage());
            throw new ApiException(ExceptionEnum.PASSWAORD_REGEX);
        }

        String password = passwordEncoder.encode(requestDto.getPassword());

        //중복된 아이디 값 체크
        Optional<User> findUser = userRepository.findByUserid(userid);

        if(findUser.isPresent()){
            sentrySupport.logSimpleMessage((ExceptionEnum.DUPLICATED_USER_NAME).getMessage());
            throw new ApiException(ExceptionEnum.DUPLICATED_USER_NAME);
        }

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

        //관리자 권한 체크
        UserRoleEnum role = UserRoleEnum.USER;

        userRepository.save(new User(requestDto.getUserid(), password, requestDto.getUsername(),
                role, image_url, requestDto.getBirthday()));
        return new StatusResponseDto("회원가입 성공");
    }

    public StatusResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        String userid = requestDto.getUserid();
        String password = requestDto.getPassword();

        Optional<User> user = userRepository.findByUserid(userid);

        //사용자 존재하는지 예외처리
        if(user.isEmpty()){
            sentrySupport.logSimpleMessage((ExceptionEnum.NOT_FOUND_USER).getMessage());
            throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
        }

        //비밀번호 확인
        if(!passwordEncoder.matches(password, user.get().getPassword())){
            sentrySupport.logSimpleMessage((ExceptionEnum.BAD_REQUEST).getMessage());
            throw new ApiException(ExceptionEnum.BAD_REQUEST);
        }

        //아이디 정보로 토큰 생성
        TokenDto tokenDto = jwtUtil.creatAllToken(userid, user.get().getRole());

        //Refresh 토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserid(userid);

        if(refreshToken.isPresent()){
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else{
          RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), userid);
          refreshTokenRepository.save(newToken);
        }

        //response 헤더에 AccessToken / RefreshToken
        response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());

        return new StatusResponseDto("로그인 성공");
    }

    public StatusResponseDto logout(User user, HttpServletRequest request) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserid(user.getUserid());
        String accessToken = request.getHeader("ACCESS_KEY").substring(7);
        if(refreshToken.isPresent()){
            Long tokenTime = jwtUtil.getExpirationTime(accessToken);
            refreshTokenRepository.deleteByUserid(user.getUserid());
            return new StatusResponseDto("로그아웃 성공");
        }
        sentrySupport.logSimpleMessage((ExceptionEnum.NOT_FOUND_USER).getMessage());
        throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
    }


    //친구 목록 조회
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsers(String userid) {
        Optional<User> findUser = userRepository.findByUserid(userid);
        if(findUser.isPresent()) {
            List<User> userList = userRepository.findAllByOrderByUsernameDesc();
            return userList.stream().map(UserResponseDto::new).collect(Collectors.toList());
        }
        sentrySupport.logSimpleMessage((ExceptionEnum.UNAUTHORIZED).getMessage());
        throw new ApiException(ExceptionEnum.UNAUTHORIZED);
    }

    //특정 친구 조회
    @Transactional(readOnly = true)
    public UserResponseDto findUserInfo(String userid){
        Optional<User> findUser = userRepository.findByUserid(userid);

        if(findUser.isPresent()){
            return new UserResponseDto(findUser.get());
        }
        sentrySupport.logSimpleMessage((ExceptionEnum.NOT_FOUND_USER).getMessage());
        throw new ApiException(ExceptionEnum.NOT_FOUND_USER);
    }

    // 마이페이지 조회
    @Transactional(readOnly = true)
    public ResponseEntity<UserResponseDto> myPage(User user) {
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(user));
    }

    // 마이페이지 수정
    @Transactional
    public ResponseEntity<UserResponseDto> updateMypage(UserRequestDto userRequestDto, MultipartFile image, User user)throws IOException {

        String username = userRequestDto.getUsername();
        String birthday = userRequestDto.getBirthday();

        User findUser = userRepository.findById(user.getId()).orElseThrow(
                () -> new ApiException(ExceptionEnum.NOT_FOUND_USER)
        );

        if(!userRequestDto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$")){ // 비밀번호 정규식 체크
            sentrySupport.logSimpleMessage((ExceptionEnum.PASSWAORD_REGEX).getMessage());
            throw new ApiException(ExceptionEnum.PASSWAORD_REGEX);
        }

        String password = passwordEncoder.encode(userRequestDto.getPassword());

        //새로운 파일명 부여를 위한 현재 시간 알아내기
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        //기존 이미지 url로 설정
        String image_url = user.getImage_url();

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

        findUser.update(userRequestDto, image_url);
        return ResponseEntity.status(HttpStatus.OK).body(new UserResponseDto(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> checkUserByBirthday(String userid) {
        List<User> userList = userRepository.findByUserAndBirthday();
        return userList.stream().map(UserResponseDto::new).collect(Collectors.toList());
    }
}
