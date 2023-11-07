package gov.sg.tech.controller.websocket;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.*;
import gov.sg.tech.service.RestaurantChoiceService;
import gov.sg.tech.service.SessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class SessionWebSocketController {

    private final SessionService sessionService;

    private final RestaurantChoiceService restaurantChoiceService;

    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/join")
    @SendTo("/topic/sessions/{sessionId}/join")
    public SessionResponse joinSession(@DestinationVariable String sessionId,
                                       @Payload @Valid JoinSessionRequest joinSessionRequest) {
        return sessionService.joinSession(sessionId, joinSessionRequest);
    }

    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/manage")
    @SendTo("/topic/sessions/{sessionId}/manage")
    public SessionResponse manageSession(@DestinationVariable String sessionId,
                                         @Payload @Valid ManageSessionRequestMessage sessionRequestMessage) {
        return sessionService.manageSession(sessionId, sessionRequestMessage);
    }

    @ControllerLogger
    @PostMapping("/sessions/{sessionId}/restaurantChoice")
    @SendTo("/topic/sessions/{sessionId}/restaurantChoice")
    public UserResponse submitRestaurantChoice(@DestinationVariable String sessionId,
                                               @Payload @Valid SubmitRestaurantChoiceRequest choice) {
        return restaurantChoiceService.submitRestaurantChoice(sessionId, choice);
    }
}
