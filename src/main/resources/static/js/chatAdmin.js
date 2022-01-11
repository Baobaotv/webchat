'use strict';


var messageFormServer = document.querySelector('#messageFormServer');
//var lstShowMessage = document.querySelectorAll('#showMessage');

var messageInputServer = document.querySelector('#messageInputServer');
var elementChat = document.querySelector('#elementChat');
var connectingElement = document.querySelector('#connecting');
var showLstUser = document.querySelector('#showLstUser');
var idUser;

var btnCallServer = document.querySelector('#btnCallServer');

var stompClient = null;
var username = null;
var peer = new Peer(); 
peer.on('open',id=> {console.log(id)});
var peerId=null;

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

btnCallServer.onclick = function() {
	 var urlpath=window.location.origin+"/callUser";
	 $.ajax({
	  	   url: urlpath,
	  	   data: idUser,
	  	   contentType: "application/json",
	  	   success: function(data,response) {
	  		  var peerId=data.peer;
	  		 openStream()
	 	    .then(stream=>{
	 	        playstream('localStreamServer',stream);
	 	        const call = peer.call(peerId,stream);
	 	        call.on('stream',remoteStream=>playstream('remoteStreamServer',remoteStream));
	 	    })
	  	   },
	  	   type: 'POST'
	  	});
	//	
	   
};

//peer.on('call',call=>{
//	 call.on('stream',remoteStream=>{
//		 var teststrem= remoteStream;
//		 playstream('remoteStreamServer',remoteStream)
//	 })
//})

peer.on('call',call=>{
    openStream()
    .then(stream=>{
        call.answer(stream);
        playstream('localStreamServer',stream);
        call.on('stream',remoteStream=>playstream('remoteStreamServer',remoteStream))
    })
})



function connect(e) {

    
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

// Connect to WebSocket Server.
connect();

function onConnected() {
    // Subscribe to the Public Topic
	
//		stompClient.subscribe('/topic/publicChatRoom', onMessageReceived);
	
		stompClient.subscribe('/topic/public', onMessageReceived);
		peer.on('open',id=> {peerId=id+"";
		stompClient.send("/app/chat.addUser",
		      {},
		     JSON.stringify({sender: "server", type: 'JOIN', peerId: peerId,senderId: 1})
		  )
		});

}


function onError(error) {
//    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
//    connectingElement.style.color = 'red';
	alert('loi')
}


function sendMessage(event) {
    var messageContent = messageInputServer.value.trim();
    if(messageContent && stompClient) {
    	username = document.querySelector('#userName').innerText.trim();
        var chatMessage = {
            sender: 'server',
            receiver: username,
            senderId: '1',
            receiverId: idUser,
            content: messageInputServer.value,
//            type: 'CHAT'
        };
        stompClient.send("/app/sendToUSer", {}, JSON.stringify(chatMessage));
        var insertElement='<li class="clearfix">'+'<div class="message-data text-right">'+'<span class="message-data-time">10:10 AM, Today</span>'+'<img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="avatar">'+'</div> <div class="message other-message float-right">'+messageContent +' </div>'+'</li>'
        elementChat.innerHTML=elementChat.innerHTML+insertElement;
        messageInputServer.value = '';
    }
    event.preventDefault();
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    if(message.senderId==idUser){
    	 var insertElement='<li class="clearfix">'+'<div class="message-data">'+'<span class="message-data-time">'+message.createdDate+'</span>'+'</div> <div class="message my-message">'+message.content +' </div>'+'</li>' ;
    	    elementChat.innerHTML=elementChat.innerHTML+insertElement;
    }
    	var userId=message.senderId+"";
    	  var urlpath=window.location.origin+"/showLstUser";
    	    $.ajax({
    	    	   url: urlpath,
    	    	   data: userId,
    	    	   error: function() {
    	    	      $('#info').html('<p>An error has occurred</p>');
    	    	   },
    	    	  
    	    	   contentType: "application/json",
    	    	   success: function(data,response) {
    	    		   console.log("call success");
    	    		   var lstShowMessage = document.querySelectorAll('.searchMesssage');

    	    			   for(var i=0;i<lstShowMessage.length;i++){
    	    				   if(lstShowMessage[i].childNodes[4].defaultValue==message.senderId){
    	    					   lstShowMessage[i].remove();
    	    				   }
    	    				  
    	    			   }
    	    			
        	    			   var insertElement;
        	    				   insertElement='<li class="clearfix searchMesssage" ><img '+
        	    				   'src="https://bootdey.com/img/Content/avatar/avatar1.png" alt="avatar">' +
        	    				   ' <div class="about"> '+
        	    				   ' <div class="name"  >'+data.username+'</div>'+
        	    				 
        	    				   '<div class="status"> '+' <i class="fa fa-circle offline"></i> left 7 mins ago </div> </div>'
        	    				    +' <input value="'+data.id+'" class= "idUser" type="hidden" ></input>'+'</li>'     
        	   
        	    				   showLstUser.innerHTML=insertElement+showLstUser.innerHTML;
        	    		
    	    		   selectUser();
    	    	   },
    	    	   type: 'POST'
    	    	});
    
    
   
   
}
 

function messageUser(event) {
    var idValue= idUser;
    
    var urlpath=window.location.origin+"/chatAdmin";

    $.ajax({
    	   url: urlpath,
    	   data: idValue,
    	   error: function() {
    	      $('#info').html('<p>An error has occurred</p>');
    	   },
    	  
    	   contentType: "application/json",
    	   success: function(data,response) {
    		   var lstShowMessage = document.querySelectorAll('#showMessage');
    		   lstShowMessage.forEach(element =>{
    			   element.remove();
    		   })
    		 

    		   document.querySelector('#userName').textContent=data[0].username;
    		   data[1].forEach(element => {
    			   var insertElement;
    			   if(element.senderId==1){
    				   insertElement='<li class="clearfix" id="showMessage">'+'<div class="message-data text-right">'+'<span class="message-data-time">'+element.createdDate+'</span>'+'<img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="avatar">'+'</div> <div class="message other-message float-right">'+element.content +' </div>'+'</li>'
       		        
    			   }else{
    				   insertElement='<li class="clearfix" id="showMessage">'+'<div class="message-data">'+'<span class="message-data-time">'+element.createdDate+'</span>'+'</div> <div class="message my-message">'+element.content +' </div>'+'</li>'     
    			   }
    			   elementChat.innerHTML=elementChat.innerHTML+insertElement;
    			   
    		   });
    	     console.log("oke");
    	   },
    	   type: 'POST'
    	});
}



messageFormServer.addEventListener('submit', sendMessage, true);
//searchMesssage.addEventListener('click', messageUser, true);

function selectUser() {
	var searchMesssage = document.querySelectorAll('.searchMesssage ');
	searchMesssage.forEach((element) => {
        element.onclick= function(){
         
           idUser = this.childNodes[4].defaultValue;
           messageUser()
           
        }
        
    });
}
selectUser();