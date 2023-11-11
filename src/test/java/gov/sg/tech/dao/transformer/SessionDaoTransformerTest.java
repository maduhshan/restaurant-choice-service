package gov.sg.tech.dao.transformer;

import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.entity.Session;
import gov.sg.tech.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SessionDaoTransformerTest {

    @InjectMocks
    private SessionDaoTransformer transformer;

    @Test
    void testTransformToSessionEntity() {
        // given
        final var stubCreateSessionReq = getStubCreateSessionReq();
        final var expected = getStubSession();
        // when
        final var actual = transformer.transformToSessionEntity(stubCreateSessionReq);

        // then
        assertAll("Validate transformed entity",
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getName(), actual.getName()),
                () -> assertEquals(expected.isEnded(), actual.isEnded())
        );
    }

    private CreateSessionRequest getStubCreateSessionReq() {
        return CreateSessionRequest.builder()
                .sessionName("Lunch")
                .sessionOwnerId(4L)
                .build();
    }

    private Session getStubSession() {
        return Session.builder()
                .ended(false)
                .name("Lunch")
                .build();
    }
}
