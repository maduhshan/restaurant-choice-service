const queryString = window.location.search;
const urlParams = new URLSearchParams(queryString);

const restPick = null;

const sessionOwner = $.session.get('sessionOwner');
const sessionName = $.session.get('sessionName');
const userId = $.session.get('userId');
const sessionId = urlParams.get('sessionId');


const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/restaurant-choice-service/restaurantPicker'
});

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

window.onload = function() {
 stompClient.activate();
  $("#close").prop("disabled", !(sessionOwner.toLowerCase() === 'true'));
};

async function joinSession() {
   if(!sessionName) {
        await stompClient.publish({
            destination: "/app/sessions/"+sessionId+"/join",
            body: JSON.stringify({'userId': userId})
        });
   }

}
function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    joinSession();

    stompClient.subscribe('/topic/sessions/manage/'+sessionId, (response) => {
         let responseBody = JSON.parse(response.body);
         setSessionName(responseBody);

         for (const user of responseBody.users) {
            if(user.restaurantChoice) {
               showRestaurants(user.username + "'s choice is " + user.restaurantChoice);
            } else {
               showRestaurants(user.username + " Joined");
            }
         }

         if(responseBody.restaurantChoice){
          $("#restPick").html(responseBody.restaurantChoice );
         }

         if(responseBody.restaurantChoice && responseBody.ended){
           endSession();
         }

    });
};

function setSessionName(responseBody) {
        if(!sessionName) {
         $("#sessionId").html(responseBody.sessionName + " - " + sessionId);
        }
        $("#restaurants").html("");
}

function sendRestName(){
       const restName =  $("#restName").val();
       stompClient.publish({
           destination: "/app/sessions/"+sessionId+"/restaurantChoice",
           body: JSON.stringify({'userId': userId, 'restaurantChoiceName' : restName})
       });

}

function closeRoom(){
   const sessionId = urlParams.get('sessionId');
   stompClient.publish({
             destination: "/app/sessions/"+ sessionId+"/manage",
             body: JSON.stringify({'userId': userId, 'operationType' : 'end'})
    });
}
function endSession() {
         $("#restName").prop("disabled", true);
         $("#send").prop("disabled", true);
         $("#close").prop("disabled", true);
         $("#sessionId").html("Session Ended");
         $.session.remove('sessionOwner');
         $.session.remove('sessionName');
         $.session.remove('userId');
}
function showRestaurants(message) {
    $("#restaurants").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
//    $( "#create" ).click(() => createSessionRoom());
//    $( "#join" ).click(() => joinSessionRoom());
    $( "#close" ).click(() => closeRoom());
    $( "#send" ).click(() => sendRestName());
    if(sessionName) {
       $("#sessionId").html(sessionName + " - " + sessionId);
    }

});




