package gov.sg.tech.controller.transformer;

import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDataTransformerTest {

    @InjectMocks
    private UserDataTransformer userDataTransformer;

    @Test
    void testTransformToSessionResponse() {
        // given
        final var expected = getStubUserResponse();
        // when
        final var actual = userDataTransformer.transformToFullUserResponse(getStubUserData());

        // then
        assertAll("Validate transformed response",
                () -> assertNotNull(actual),
                () -> assertEquals(expected, actual)
        );
    }

    private UserData getStubUserData() {
        return UserData.builder()
                .userId(4L)
                .username("Jon Snow")
                .build();
    }

    private UserResponse getStubUserResponse() {
        return UserResponse.builder()
                .userId(4L)
                .username("Jon Snow")
                .build();
    }
}
