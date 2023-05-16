package com.example.cloneproject15.service;

import com.example.cloneproject15.dto.KakaoUserInfoDto;
import com.example.cloneproject15.dto.TokenDto;
import com.example.cloneproject15.entity.RefreshToken;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.entity.UserRoleEnum;
import com.example.cloneproject15.jwt.JwtUtil;
import com.example.cloneproject15.repository.RefreshTokenRepository;
import com.example.cloneproject15.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtUtil jwtUtil;

    public String kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        TokenDto tokenDto = getToken(code);

        String access_token = tokenDto.getAccessToken();
        String refresh_token = tokenDto.getRefreshToken();

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(access_token);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        if(kakaoUser == null){
            //아이디 정보로 토큰 생성
            tokenDto = jwtUtil.creatAllToken(kakaoUser.getUserid(), kakaoUser.getRole());

            //Refresh 토큰 있는지 확인
            Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserid(kakaoUser.getUserid());

            if(refreshToken.isPresent()){
                refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
            } else{
                RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), kakaoUser.getUserid());
                refreshTokenRepository.save(newToken);
            }
        }

        //response 헤더에 AccessToken / RefreshToken
        response.addHeader(JwtUtil.ACCESS_KEY, "Bearer "+tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_KEY, "Bearer "+tokenDto.getRefreshToken());

        return "로그인 성공";
    }

    // 1. "인가 코드"로 "액세스 토큰" 요청
    private TokenDto getToken(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "028ee58b17a56cabc49dd857ac4eef57");
        body.add("client_secret", "eFxP9dHAouovN9WB5s3F7qH2mwj3bYlB");
        body.add("redirect_uri", "http://localhost:8080/oauth/kakao");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return new TokenDto(jsonNode.get("access_token").asText(), jsonNode.get("refresh_token").asText());
    }

    // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();
        String profile_image = jsonNode.get("properties")
                        .get("profile_image").asText();


        System.out.println("카카오 정보 : "+jsonNode.toString());
        return new KakaoUserInfoDto(id, nickname, email, profile_image);
    }

    // 3. 필요시에 회원가입
    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoid(kakaoId)
                .orElse(null);
        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                String image_url = kakaoUserInfo.getProfile_image();

                String birthday = "0000-00-00";

                kakaoUser = new User("kakao", encodedPassword, kakaoUserInfo.getNickname(), UserRoleEnum.USER, kakaoId, email, image_url, birthday);
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }
}