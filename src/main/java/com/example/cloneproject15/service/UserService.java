package com.example.cloneproject15.service;

import com.example.cloneproject15.dto.TokenDto;
import com.example.cloneproject15.dto.UserRequestDto;
import com.example.cloneproject15.dto.UserResponseDto;
import com.example.cloneproject15.entity.RefreshToken;
import com.example.cloneproject15.entity.User;
import com.example.cloneproject15.entity.UserRoleEnum;
import com.example.cloneproject15.jwt.JwtUtil;
import com.example.cloneproject15.redis.RedisUtil;
import com.example.cloneproject15.repository.RefreshTokenRepository;
import com.example.cloneproject15.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public UserResponseDto signup(UserRequestDto requestDto) {

        String userid = requestDto.getUserid();
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String image_url = requestDto.getImage_url();
        String category = requestDto.getCategory();

        //중복된 아이디 값 체크
        Optional<User> user = userRepository.findByUserid(userid);

        if(user.isPresent()){
            throw new IllegalStateException("중복된 아이디가 존재합니다.");
        }

        //관리자 권한 체크
        UserRoleEnum role = UserRoleEnum.USER;

        userRepository.save(new User(userid, password, username, role, image_url, category));
        return new UserResponseDto("회원가입 성공");

    }

    public UserResponseDto login(UserRequestDto requestDto, HttpServletResponse response) {
        String userid = requestDto.getUserid();
        String password = requestDto.getPassword();

        Optional<User> user = userRepository.findByUserid(userid);

        //사용자 존재하는지 예외처리
        if(user.isEmpty()){
            throw new IllegalStateException("사용자가 존재하지 않습니다.");
        }

        //비밀번호 확인
        if(!passwordEncoder.matches(password, user.get().getPassword())){
            throw new IllegalStateException("비밀번호가 일치하지 않습니다.");
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

        return new UserResponseDto("로그인 성공");
    }

    public UserResponseDto logout(User user, HttpServletRequest request) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByUserid(user.getUserid());
        String accessToken = request.getHeader("ACCESS_KEY").substring(7);
        if(refreshToken.isPresent()){
            Long tokenTime = jwtUtil.getExpirationTime(accessToken);
            redisUtil.setBlackList(accessToken, "access_token", tokenTime);
            refreshTokenRepository.deleteByUserid(user.getUserid());
            return new UserResponseDto("로그아웃 성공");
        }
        throw new IllegalStateException("로그아웃 실패");
    }
}
