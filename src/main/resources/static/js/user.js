 export async function createSessionRoom() {
   const user = await makePostCall('http://localhost:8080/restaurant-choice-service/users', {username: $("#name").val()});

   let userId = user.userId;
   const session = await makePostCall('http://localhost:8080/restaurant-choice-service/sessions', {sessionName: $("#session").val(),
                                                                                userId: userId});
  let sessionOwner = true;
  let sessionId = session.sessionId;
  let sessionName = session.sessionName;

  $.session.set('sessionOwner', true);
  $.session.set('sessionName', sessionName);
  $.session.set('userId', userId);

  const queryString = `?sessionId=${encodeURIComponent(sessionId)}`;
  window.location.href = `./pages/session.html${queryString}`;
}

 export async function joinSessionRoom() {
  const user = await makePostCall('http://localhost:8080/restaurant-choice-service/users', {username: $("#name").val()});
  let userId = user.userId;
  let sessionId = $("#session").val();
  $.session.set('sessionOwner', false);
  $.session.set('userId', userId);

  const queryString = `?sessionId=${encodeURIComponent(sessionId)}`;
  window.location.href = `./pages/session.html${queryString}`;
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