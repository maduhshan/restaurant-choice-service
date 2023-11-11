package gov.sg.tech.controller.transformer;

import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.domain.pojo.SessionData;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * This is a Transformer which transforms SessionData Pojo to REST understandable
 * SessionResponse Object
 *
 * @author Madushan
 */
@RequiredArgsConstructor
@Component
public class SessionDataTransformer {

    private final UserDataTransformer userDataTransformer;

    /**
     * Transforms SessionData Pojo to SessionResponse
     *
     * @param session session data
     * @return transformed session response
     */
    public SessionResponse transformToSessionResponse(@NotNull SessionData session) {
        return SessionResponse.builder()
                .sessionId(session.getSessionId())
                .sessionName(session.getSessionName())
                .ended(session.isEnded())
                .restaurantChoice(session.getRestaurantChoice())
                .users(session.getUsers()
                        .stream()
                        .map(userDataTransformer::transformToFullUserResponse)
                        .collect(Collectors.toSet()))
                .build();
    }
}
