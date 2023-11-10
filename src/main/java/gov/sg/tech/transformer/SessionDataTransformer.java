package gov.sg.tech.transformer;

import gov.sg.tech.domain.CreateSessionRequest;
import gov.sg.tech.domain.SessionResponse;
import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.entity.Session;
import gov.sg.tech.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;

@Component
public class SessionDataTransformer {

    public Session transformToSessionEntity(CreateSessionRequest createSessionRequest) {
        return Session.builder()
                .ended(false)
                .name(createSessionRequest.getSessionName())
                .id((long) (new Random().nextInt(900000) + 100000))
                .build();
    }

    public SessionResponse transformToSessionResponse(Session session) {
        return SessionResponse.builder()
                .sessionId(session.getId())
                .sessionName(session.getName())
                .ended(session.isEnded())
                .restaurantChoice(session.getUsers()
                        .stream()
                        .filter(User::isWinner)
                        .findFirst().map(User::getRestaurantChoice).orElse(null))
                .users(session.getUsers()
                        .stream()
                        .map(this::transformToThinUserResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    private UserResponse transformToThinUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getName())
                .restaurantChoice(user.getRestaurantChoice())
                .build();
    }
}
