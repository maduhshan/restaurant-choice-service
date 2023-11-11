package gov.sg.tech.controller.transformer;

import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.domain.pojo.SessionData;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class SessionDataTransformer {

    private final UserDataTransformer userDataTransformer;

    public SessionResponse transformToSessionResponse(@Nonnull SessionData session) {
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
