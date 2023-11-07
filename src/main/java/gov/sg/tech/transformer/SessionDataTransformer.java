package gov.sg.tech.transformer;

import gov.sg.tech.domain.CreateSessionRequest;
import gov.sg.tech.domain.SessionResponse;
import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.entity.Session;
import gov.sg.tech.entity.User;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class SessionDataTransformer {

    public Session transformToSessionEntity(CreateSessionRequest createSessionRequest) {
        return Session.builder()
                .ended(false)
                .name(createSessionRequest.getSessionName())
                .sessionOwner(User.builder()
                        .username(createSessionRequest.getSessionOwner())
                        .build())
                .sessionId(UUID.randomUUID())
                .build();
    }

    public SessionResponse transformToSessionResponse(Session session) {
        return SessionResponse.builder()
                .sessionId(String.valueOf(session.getSessionId()))
                .sessionName(session.getName())
                .createdAt(session.getCreatedAt().toLocalDateTime())
                .ended(session.isEnded())
                .restaurantChoice(session.getSelectedRestaurantChoice())
                .users(session.getUsers()
                        .stream()
                        .map(this::transformToThinUserResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    private UserResponse transformToThinUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId().toString())
                .username(user.getUsername())
                .build();

    }
}
