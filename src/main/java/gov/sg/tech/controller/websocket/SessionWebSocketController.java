package gov.sg.tech.controller.websocket;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.*;
import gov.sg.tech.service.UserService;
import gov.sg.tech.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class SessionWebSocketController {

    private final SessionService sessionService;

    private final UserService userService;


    @ControllerLogger
    @MessageMapping("/sessions/createRoom")
    @SendTo("/topic/sessions/manage")
    public SessionResponse createRoom(@Payload @Valid CreateSessionRequest createSessionRequest) {
        return sessionService.createSession(createSessionRequest);

    }

    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/join")
    @SendTo("/topic/sessions/manage")
    public SessionResponse joinSession(@DestinationVariable Long sessionId,
                                       @Payload @Valid JoinSessionRequest joinSessionRequest) {
        return sessionService.joinSession(sessionId, joinSessionRequest);

    }

    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/manage")
    @SendTo("/topic/sessions/manage")
    public SessionResponse manageSession(@DestinationVariable Long sessionId,
                                  @Payload @Valid ManageSessionRequestMessage sessionRequestMessage) {
        return sessionService.manageSession(sessionId, sessionRequestMessage);
    }

    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/restaurantChoice")
    @SendTo("/topic/sessions/manage")
    public SessionResponse submitRestaurantChoice(@DestinationVariable Long sessionId,
                                               @Payload @Valid SubmitRestaurantChoiceRequest choice) {
        return sessionService.submitRestaurantChoice(sessionId, choice);
    }



}
