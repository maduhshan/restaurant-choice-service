package gov.sg.tech.controller.rest;

import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.SessionData;
import gov.sg.tech.domain.pojo.UserData;
import gov.sg.tech.exception.ResourceNotFoundException;
import gov.sg.tech.service.SessionService;
import gov.sg.tech.controller.transformer.SessionDataTransformer;
import gov.sg.tech.util.ObjectMapperSingleton;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SessionRestControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionDataTransformer sessionDataTransformer;

    @InjectMocks
    private SessionRestController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @SneakyThrows
    @DisplayName("Test Create Session Success Scenario")
    @Test
    void testCreateSessionSuccess() {
        // given
        final var stubCreateSessionReq = getStubCreateSessionReq();
        final var stubSessionData = getStubSessionData();
        final var expected = getStubSessionResponse();
        when(sessionService.createSession(any(CreateSessionRequest.class))).thenReturn(stubSessionData);
        when(sessionDataTransformer.transformToSessionResponse(any(SessionData.class))).thenReturn(expected);
        // when
        final var actual = mockMvc.perform(post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperSingleton.getInstance().writeValueAsString(stubCreateSessionReq)))
                .andExpect(status().isCreated())
                .andExpect(content().json(ObjectMapperSingleton.getInstance().writeValueAsString(expected)));

        // then
        assertNotNull(actual, "Response is not null");
        verify(sessionService, times(1)).createSession(any(CreateSessionRequest.class));
        verify(sessionDataTransformer, times(1)).transformToSessionResponse(any(SessionData.class));
    }

    @SneakyThrows
    @DisplayName("Test Create Session When Invalid Session Owner Given")
    @Test
    void testCreateSessionWhenInvalidSessionOwnerGiven() {
        // given
        final var stubCreateSessionReq = getStubCreateSessionReq();

        when(sessionService.createSession(any(CreateSessionRequest.class)))
                .thenThrow(ResourceNotFoundException.class);
        // when
        mockMvc.perform(post("/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperSingleton.getInstance().writeValueAsString(stubCreateSessionReq)))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @DisplayName("Test Get Session Success Scenario")
    @Test
    void testGetSessionSuccess() {
        // given
        final var expected = getStubSessionResponse();
        final var stubSessionData = getStubSessionData();
        when(sessionService.getSessionById(anyLong())).thenReturn(stubSessionData);
        when(sessionDataTransformer.transformToSessionResponse(any(SessionData.class))).thenReturn(expected);

        // when
        final var actual = mockMvc.perform(get("/sessions/1234")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(ObjectMapperSingleton.getInstance().writeValueAsString(expected)));

        // then
        assertNotNull(actual, "Response is not null");
        verify(sessionService, times(1)).getSessionById(anyLong());
        verify(sessionDataTransformer, times(1)).transformToSessionResponse(any(SessionData.class));
    }

    private CreateSessionRequest getStubCreateSessionReq() {
        return CreateSessionRequest.builder()
                .sessionName("Monday Lunch")
                .sessionOwnerId(4L)
                .build();
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
