const auth = 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMTIiLCJhdXRoIjoiVVNFUiIsImV4cCI6MTY4NDE1MzkyMCwiaWF0IjoxNjg0MTUwMzIwfQ.0RvNclwAzcsDFUGho8lRzDFH9K_myni5feM-A2tXS88';

// 처음 로딩 시 사용자 정보 가져오기 (이름 및 폴더)
if(auth !== '') {
    // 로그인한 유저 이름
    $.ajax({
        type: 'GET',
        url: `/mypage`,
        beforeSend: function (xhr) {
            xhr.setRequestHeader("ACCESS_KEY", auth);
        },
        success: function (response) {
            console.log(response);
            $("#my-name").val(response['username']);
        },
        error(error, status, request) {
            console.error(error);
        }
    });
}


var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/ws-edit');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

    });
}

function enterChatRoom() {
    var roomId = $("#connectRoomId").val();

    stompClient.subscribe('/sub/chat/room' + roomId, function (message){
        showGreeting("Sender : " + JSON.parse(message.body).sender + "</br>" +
            "Message : " + JSON.parse(message.body).message)});

    stompClient.send("/pub/chat/enter", {}, JSON.stringify(
        { 'type' : "ENTER",
            'sender' : $("#my-name").val(),
            'roomId' : $("#connectRoomId").val(),
            'message': ""}));
}

function sendMessage() {
    stompClient.send("/pub/chat/send", {}, JSON.stringify(
        { 'type' : "TALK",
            'sender' : $("#my-name").val(),
            'roomId' : $("#connectRoomId").val(),
            'message': $("#my-message").val()}));
}

function leaveChatRoom() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendMessage(); });
    $( "#enter" ).click(function () { enterChatRoom(); })
    $( "#leave" ).click(function () { leaveChatRoom(); })
});
