package gov.sg.tech.controller.transformer;

import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.SessionData;
import gov.sg.tech.domain.pojo.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionDataTransformerTest {

    @Mock
    private UserDataTransformer userDataTransformer;

    @InjectMocks
    private SessionDataTransformer transformer;

    @Test
    void testTransformToSessionResponse() {
        // given
        final var expected = getStubSessionResponse();
        when(userDataTransformer.transformToFullUserResponse(any(UserData.class)))
                .thenReturn(getStubUserResponse());

        // when
        final var actual = transformer.transformToSessionResponse(getStubSessionData());

        // then
        assertAll("Validate transformed response",
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
    }

    private SessionResponse getStubSessionResponse() {
        return SessionResponse.builder()
                .sessionId(1234L)
                .sessionName("Monday Lunch")
                .users(Collections.singleton(UserResponse.builder()
                        .userId(4L)
                        .username("Jon Snow")
                        .build()))
                .build();
    }

    private SessionData getStubSessionData() {
        return SessionData.builder()
                .sessionId(1234L)
                .sessionName("Monday Lunch")
                .users(Collections.singleton(UserData.builder()
                        .userId(4L)
                        .username("Jon Snow")
                        .build()))
                .build();
    }

    private UserResponse getStubUserResponse() {
        return UserResponse.builder()
                .userId(4L)
                .username("Jon Snow")
                .build();
    }
}
