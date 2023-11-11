package gov.sg.tech.dao.transformer;

import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.entity.Session;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * Transforms Objects to DAO layer compatible entities
 *
 * @author Madushan
 */
@Component
public class SessionDaoTransformer {

    /**
     * Transforms to session entity
     *
     * @param createSessionRequest create session entity
     * @return Transformed Session entity
     */
    public Session transformToSessionEntity(CreateSessionRequest createSessionRequest) {
        return Session.builder()
                .ended(false)
                .name(createSessionRequest.getSessionName())
                .id((long) (new Random().nextInt(900000) + 100000))
                .build();
    }
}
