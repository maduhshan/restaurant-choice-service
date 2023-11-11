package gov.sg.tech.controller.rest;

import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.UserData;
import gov.sg.tech.service.UserService;
import gov.sg.tech.controller.transformer.UserDataTransformer;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserDataTransformer userDataTransformer;

    @InjectMocks
    private UserRestController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @SneakyThrows
    @DisplayName("Test Create User Success Scenario")
    @Test
    void testCreateCreateUserSuccess() {
        // given
        final var stub = getStubRegisterUserReq();
        final var expected = getStubUserResponse();
        final var stubUserData = getStubUserData();
        when(userService.registerUser(any(RegisterUserRequest.class))).thenReturn(stubUserData);
        when(userDataTransformer.transformToFullUserResponse(any(UserData.class))).thenReturn(expected);

        // when
        final var actual = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapperSingleton.getInstance().writeValueAsString(stub)))
                .andExpect(status().isCreated())
                .andExpect(content().json(ObjectMapperSingleton.getInstance().writeValueAsString(expected)));

        // then
        assertNotNull(actual, "Response is not null");
        verify(userService, times(1)).registerUser(any(RegisterUserRequest.class));
        verify(userDataTransformer, times(1)).transformToFullUserResponse(any(UserData.class));
    }

    private RegisterUserRequest getStubRegisterUserReq() {
        return RegisterUserRequest.builder()
                .username("Ned Stark").build();
    }

    private UserResponse getStubUserResponse() {
        return UserResponse.builder()
                .userId(1L)
                .username("Ned Stark").build();
    }

    private UserData getStubUserData() {
        return UserData.builder()
                .userId(1L)
                .username("Ned Stark").build();
    }
}


