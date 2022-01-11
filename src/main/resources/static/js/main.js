'use strict';


var messageForm = document.querySelector('#messageForm');

var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('#connecting');
var idUser = document.querySelector('#idUser');

var btnCallUser = document.querySelector('#btnCallUser');
var stompClient = null;
var username = null;
var peer = new Peer(); 
peer.on('open',id=> {console.log(id)});
var peerId=null;

btnCallUser.onclick = function() {
	 $.ajax({
	  	   url: 'http://localhost:8080/callUser',
	  	   data: '1',
	  	   contentType: "application/json",
	  	   success: function(data,response) {
	  		  var peerId=data.peer;
	  		 openStream()
	 	    .then(stream=>{
	 	        playstream('localStreamUser',stream);
	 	        const call = peer.call(peerId,stream);
	 	        call.on('stream',remoteStream=>playstream('remoteStreamUser',remoteStream));
	 	    })
	  	   },
	  	   type: 'POST'
	  	});
	//	
	   
};

function openStream(){
    const config={
        audio: true,
        video: true
    };
    
    return navigator.mediaDevices.getUserMedia(config);
}

function playstream(idVideo, stream){
    const video = document.getElementById(idVideo);
    video.srcObject= stream;
    video.play();
}

peer.on('call',call=>{
    openStream()
    .then(stream=>{
        call.answer(stream);
        playstream('localStreamUser',stream);
        call.on('stream',remoteStream=>playstream('remoteStreamUser',remoteStream))
    })
})



function connectCall(id){
	
}

//$('#btnCallUser').click(()=>{
//    const id= $('#remoteId').val();
//    openStream()
//    .then(stream=>{
//        playstream('localStream',stream);
//        const call = peer.call(id,stream);
//        call.on('stream',remoteStream=>playstream('remoteStream',remoteStream));
//    })
//
//})

//answer


function connect() {
    username = document.querySelector('#username').innerText.trim();
     
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    
   
    stompClient.connect({}, onConnected, onError);
  
}


// Connect to WebSocket Server.
connect();

function onConnected() {
    // Subscribe to the Public Topic
	
//	stompClient.subscribe('/topic/publicChatRoom', onMessageReceived);
	var url ="/topic/"+username;
		stompClient.subscribe(url, onMessageReceived);
	
    // Tell your username to the server
		peer.on('open',id=> {peerId=id+"";
		stompClient.send("/app/chat.addUser",
		      {},
		     JSON.stringify({sender: username, type: 'JOIN', peerId: peerId,senderId: idUser.value})
		  )
		});

    connectingElement.classList.add('hidden');
}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            senderId: idUser.value,
            receiverId:'1',
            content: messageInput.value,
//            type: 'CHAT'
        };
        stompClient.send("/app/public", {}, JSON.stringify(chatMessage));
        var messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');   
        var usernameElement = document.createElement('strong');
        usernameElement.classList.add('nickname');
        var usernameText = document.createTextNode(chatMessage.sender);
        var usernameText = document.createTextNode(chatMessage.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        var textElement = document.createElement('span');
        var messageText = document.createTextNode(chatMessage.content);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
        
        messageInput.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var messageElement = document.createElement('li');

//    if(message.type === 'JOIN') {
//        messageElement.classList.add('event-message');
//        message.content = message.sender + ' joined!';
//    } else if (message.type === 'LEAVE') {
//        messageElement.classList.add('event-message');
//        message.content = message.sender + ' left!';
//    } else {
        messageElement.classList.add('chat-message');   
        var usernameElement = document.createElement('strong');
        usernameElement.classList.add('nickname');
        var usernameText = document.createTextNode(message.sender);
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
//    }

    var textElement = document.createElement('span');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);

    messageElement.appendChild(textElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
 




messageForm.addEventListener('submit', sendMessage, true);