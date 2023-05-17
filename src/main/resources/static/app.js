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
            'userId' : $("#my_id").val(),
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
