
# miniproject_hanhae66

>개발 기간: 2023.05.12 ~ 2023.05.18</p>🎥 시연영상 [https://www.youtube.com/watch?v=mGZ9BXAWmiE](https://www.youtube.com/watch?v=AM1mPU-ozMU)

### 🖐카카오톡 클론코딩
  - Stomp로 실시간 채팅방을 구현해보자!

## 📃 S.A
https://rough-steam-f82.notion.site/S-A-e994ea0da5824fd49913e2efcf4663f3

## 📜와이어 프레임
![image](https://github.com/OliveLover/CloneProject15/assets/118647313/3502ba3e-640b-480d-bb2e-59ba1463a587)
![image](https://github.com/OliveLover/CloneProject15/assets/118647313/07d2ef01-a6cd-4917-b4a1-19d52f8c96a1)
![image](https://github.com/OliveLover/CloneProject15/assets/118647313/417eaac7-7525-4674-b5fb-1a164ae1bf45)
![image](https://github.com/OliveLover/CloneProject15/assets/118647313/0a84ba4a-01d3-4a18-91d4-2b3676f68dc3)


## 📰 ERD
![erd](https://github.com/OliveLover/CloneProject15/assets/118647313/98c1b106-6bf5-458b-bf32-84c11b0e5431)



## 📖 API 명세서
https://www.notion.so/31b54aa077d04d119ca2e5f10c3e22b7?v=8d4e491b55374a5581f611d943b653b1

## 👨‍👩‍👧팀원
|이름|역할|
|------|---|
|조유민(BE팀장)</br>[@eivomin](https://github.com/eivomin)|- 회원가입, 로그인, 로그아웃 API</br>- RefreshToken, AccessToken</br>- 서버 배포</br>- S3|
|이상언</br>[@eoneee](https://github.com/eoneee)|- 마이페이지</br>- RDS(MySQL) 연결 </br>|
|김재형</br>[@jaykim12](https://github.com/jaykim12)|-채팅 기능</br>- 검색 기능|
|이현규</br>[@OliveLover](https://github.com/OliveLover)|- 채팅 기능</br>- 검색 기능|

FE git hub : https://github.com/HyoHwanKim/hangHae99-cloneWeek

## ⚙️ Tech Stack
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <br>
<img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=black"> <img src="https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white"> <img src="https://img.shields.io/badge/css-1572B6?style=for-the-badge&logo=css3&logoColor=white"> <br>
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white"> <br>
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

## 💻 기능
회원가입, 로그인, 로그아웃</br>
</br>
채팅</br>
- 채팅방 생성
- 채팅방 입장
- 채팅방 대화
- 모든 대화상대퇴장시 채팅방 삭제
- 사진 전송</br>
</br>
5일 앞의 생일인 유저 표시</br>
</br>
마이페이지(수정기능 미완료)</br>


## 🚩 채팅기능 구현을 위해 고민한 것

STOMP를 통하여 단순한 채팅기능만 있는 형태에서 채팅방을 만드는 것에대하여 많은 고민을 해보았습니다.</br>
우리가 생각한 방법은 ChatRoom Entity를 만들어 "방생성 API"를통해 "TB_CHATROOM" 테이블과 매핑을 하였습니다.</br>
방의 순서를 특정하고 싶지 않아 시간과 관련하여 고유한 값을 주는 "UUID"를 사용하여 방의 ID를 나타내었습니다.</br>
</sub/chat/room + roomId> 으로 방ID를 구독한 대상에게만 메시지가 보내지도록 하였습니다.</br>
User가 어느 방에 들어가는지 특정하기 위하여 User에 RoomId를 연관시켜주었고, 해당 RoomId의 수를 세어
방에 어떤 User가 있는지도 알수 있게 하였습니다.</br>
이 정보를 바탕으로 방생성 후 모든 유저가 나가서 방의 유저 카운트가 0이되면 방은 자동으로 삭제됩니다.</br>
사진 전송은 POST 요청을 통하여 전송합니다.
