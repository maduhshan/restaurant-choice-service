package gov.sg.tech.dao.transformer;

import gov.sg.tech.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class UserDaoTransformerTest {

    @InjectMocks
    private UserDaoTransformer transformer;

    @Test
    void testTransformToSessionEntity() {
        // given
        final var expected = getStubUser();
        // when
        final var actual = transformer.transformToUser("Drogon", false);

        // then
        assertAll("Validate transformed user entity",
                () -> assertNotNull(actual),
                () -> assertEquals(expected.getName(), actual.getName())
        );
    }

    private User getStubUser() {
        return User.builder()
                .name("Drogon")
                .build();
    }
}
