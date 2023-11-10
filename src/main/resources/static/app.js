

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/restaurantPicker'
});

const sessionId = null;
const restPick = null;
const userName = null;
const userId = null;
const sessionOwner = false;
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/sessions/manage', (response) => {
        if(JSON.parse(response.body).sessionId === Number(this.sessionId)) {
        $("#restaurants").html("");
         for (const user of JSON.parse(response.body).users) {
            if(user.restaurantChoice) {
               showRestaurants(user.username + " choice is " + user.restaurantChoice);
            } else {
               showRestaurants(user.username);
            }
         }
        } else {
         showRestaurants('Different Chat');
         }
         if(JSON.parse(response.body).restaurantChoice){
          $("#restPick").html(JSON.parse(response.body).restaurantChoice );
         }

    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

window.onload = function() {
 connect();
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#restaurants").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

 async function createSessionRoom() {
   const user = await makePostCall('http://localhost:8080/users', {username: $("#name").val()});

   this.userId = user.userId;
   const session = await makePostCall('http://localhost:8080/sessions', {sessionName: $("#sessionName").val(),
                                                                                sessionOwnerId: this.userId});
  this.sessionOwner = true;
  this.sessionId = session.sessionId;
  $("#sessionId").html(session.sessionId );
  this.sessionName = session.sessionName;
  showRestaurants(user.username);
}

async function makePostCall(url, data) {
  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Error making POST request: ${response.statusText}`);
  }

  return response.json();
}

 async function joinSessionRoom() {
   const user = await makePostCall('http://localhost:8080/users', {username: $("#name").val()});
   this.userId = user.userId;

   this.sessionId = $("#joinSessionId").val();

   await stompClient.publish({
       destination: "/app/sessions/"+this.sessionId+"/join",
       body: JSON.stringify({'userId': this.userId})
   });
    $("#sessionId").html(sessionId );
 }

function sendRestName(){
       var restName =  $("#restName").val();
       stompClient.publish({
           destination: "/app/sessions/"+this.sessionId+"/restaurantChoice",
           body: JSON.stringify({'userId': this.userId, 'restaurantChoiceName' : restName})
   });
}

function closeRoom(){
   stompClient.publish({
             destination: "/app/sessions/"+this.sessionId+"/manage",
             body: JSON.stringify({'userId': this.userId, 'operationType' : 'end'})
    });
}

function showRestaurants(message) {
    $("#restaurants").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#create" ).click(() => createSessionRoom());
    $( "#join" ).click(() => joinSessionRoom());
    $( "#close" ).click(() => closeRoom());
    $( "#send" ).click(() => sendRestName());

});

