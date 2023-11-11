package gov.sg.tech.controller.websocket;

import gov.sg.tech.controller.transformer.SessionDataTransformer;
import gov.sg.tech.domain.dto.JoinSessionRequest;
import gov.sg.tech.domain.dto.ManageSessionRequest;
import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.domain.dto.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.SessionData;
import gov.sg.tech.domain.pojo.UserData;
import gov.sg.tech.service.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionWebSocketControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionDataTransformer sessionDataTransformer;

    @InjectMocks
    private SessionWebSocketController webSocketController;

    @Test
    void testJoinSession() {
        // given
        final var stubJoinSessionReq = JoinSessionRequest.builder()
                .userId(1L).build();
        final var expected = getStubSessionResponse();
        when(sessionService.joinSession(anyLong(), eq(stubJoinSessionReq))).thenReturn(getStubSessionData());
        when(sessionDataTransformer.transformToSessionResponse(any(SessionData.class)))
                .thenReturn(expected);

        // when
        final var actual = webSocketController.joinSession(1234L, stubJoinSessionReq);

        // actual
        assertAll("Validate join session response",
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
        verify(sessionService, times(1)).joinSession(eq(1234L), any(JoinSessionRequest.class));
        verify(sessionDataTransformer, times(1)).transformToSessionResponse(any(SessionData.class));
    }

    @Test
    void testManageSession() {
        // given
        final var stubManageSessionReq = ManageSessionRequest.builder()
                .operationType("end")
                .userId(null).build();
        final var expected = getStubSessionResponse();
        when(sessionService.manageSession(anyLong(), eq(stubManageSessionReq))).thenReturn(getStubSessionData());
        when(sessionDataTransformer.transformToSessionResponse(any(SessionData.class)))
                .thenReturn(expected);
        // when
        final var actual = webSocketController.manageSession(1234L, stubManageSessionReq);

        // actual
        assertAll("Validate join session response",
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
        verify(sessionService, times(1)).manageSession(eq(1234L), any(ManageSessionRequest.class));
        verify(sessionDataTransformer, times(1)).transformToSessionResponse(any(SessionData.class));
    }

    @Test
    void testSubmitRestaurantChoice() {
        // given
        final var stubManageSessionReq = SubmitRestaurantChoiceRequest.builder()
                .restaurantChoiceName("KFC")
                .userId(null).build();
        final var expected = getStubSessionResponse();
        when(sessionService.submitRestaurantChoice(anyLong(), eq(stubManageSessionReq)))
                .thenReturn(getStubSessionData());
        when(sessionDataTransformer.transformToSessionResponse(any(SessionData.class)))
                .thenReturn(expected);
        // when
        final var actual = webSocketController.submitRestaurantChoice(1234L, stubManageSessionReq);

        // actual
        assertAll("Validate join session response",
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
        verify(sessionService, times(1))
                .submitRestaurantChoice(eq(1234L), any(SubmitRestaurantChoiceRequest.class));
        verify(sessionDataTransformer, times(1)).transformToSessionResponse(any(SessionData.class));
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
}
