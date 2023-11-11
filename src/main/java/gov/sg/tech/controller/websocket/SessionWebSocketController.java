package gov.sg.tech.controller.websocket;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.dto.JoinSessionRequest;
import gov.sg.tech.domain.dto.ManageSessionRequest;
import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.domain.dto.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.pojo.SessionData;
import gov.sg.tech.service.SessionService;
import gov.sg.tech.controller.transformer.SessionDataTransformer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * WebSocket Controller for message mapping and destination publishing
 * on Session Operations
 *
 * @author Madushan
 */
@RequiredArgsConstructor
@Controller
public class SessionWebSocketController {

    private final SessionService sessionService;

    private final SessionDataTransformer sessionDataTransformer;

    /**
     * WebSocket message mapping method to join a session
     *
     * @param sessionId session id
     * @param joinSessionRequest join session payload which contains user id
     * @return SessionResponse to be send over to destination
     */
    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/join")
    @SendTo("/topic/sessions/manage/{sessionId}")
    public SessionResponse joinSession(@DestinationVariable Long sessionId,
                                       @Payload @Valid JoinSessionRequest joinSessionRequest) {
        SessionData sessionData = sessionService.joinSession(sessionId, joinSessionRequest);
        return sessionDataTransformer.transformToSessionResponse(sessionData);

    }

    /**
     * WebSocket message mapping method to manage/end a session
     *
     * @param sessionId session id
     * @param sessionRequestMessage manage session payload which contains user id and session operation
     * @return SessionResponse to be send over to destination
     */
    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/manage")
    @SendTo("/topic/sessions/manage/{sessionId}")
    public SessionResponse manageSession(@DestinationVariable Long sessionId,
                                  @Payload @Valid ManageSessionRequest sessionRequestMessage) {
        SessionData sessionData = sessionService.manageSession(sessionId, sessionRequestMessage);
        return sessionDataTransformer.transformToSessionResponse(sessionData);
    }

    /**
     * WebSocket message mapping method to submit a restaurant choice
     *
     * @param sessionId session id
     * @param choice request payload which contains user id and restaurant choice
     * @return SessionResponse to be send over to destination
     */
    @ControllerLogger
    @MessageMapping("/sessions/{sessionId}/restaurantChoice")
    @SendTo("/topic/sessions/manage/{sessionId}")
    public SessionResponse submitRestaurantChoice(@DestinationVariable Long sessionId,
                                               @Payload @Valid SubmitRestaurantChoiceRequest choice) {
        SessionData sessionData = sessionService.submitRestaurantChoice(sessionId, choice);
        return sessionDataTransformer.transformToSessionResponse(sessionData);
    }
}
