package gov.sg.tech.service;

import gov.sg.tech.dao.transformer.UserDaoTransformer;
import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.pojo.UserData;
import gov.sg.tech.entity.User;
import gov.sg.tech.dao.repository.UserRepository;
import gov.sg.tech.service.impl.UserServiceImpl;
import org.hibernate.HibernateException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDaoTransformer userDaoTransformer;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegisterUser() {
        // given
        final var userRequest = getStubRegisterUserReq();
        final var stubUser = getStubUser(1L, "Ned Stark", false, null);
        final var expected = getStubUserData();
        when(userDaoTransformer.transformToUser(anyString(), anyBoolean())).thenReturn(stubUser);
        when(userRepository.save(any(User.class))).thenReturn(stubUser);

        // when
        final var actual = userService.registerUser(userRequest);

        // then
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(expected, actual);
    }

    @Test
    void testRegisterUserWhenDbErrorOccurs() {
        // given
        final var userRequest = getStubRegisterUserReq();
        final var stub = mock(User.class);
        final var expected = getStubUserData();
        when(userDaoTransformer.transformToUser(anyString(), anyBoolean())).thenReturn(stub);
        when(userRepository.save(any(User.class))).thenThrow(HibernateException.class);

        // when
        final var exception = assertThrows(HibernateException.class,
                () -> userService.registerUser(userRequest));

        // then
        assertEquals(exception.getClass(), HibernateException.class);
    }

    private User getStubUser(Long id, String name, boolean isOwner, String restaurant) {
        return User.builder()
                .id(id)
                .name(name)
                .isOwner(isOwner)
                .restaurantChoice(restaurant)
                .build();
    }

    private RegisterUserRequest getStubRegisterUserReq() {
        return RegisterUserRequest.builder()
                .username("Ned Stark").build();
    }

    private UserData getStubUserData() {
        return UserData.builder()
                .userId(1L)
                .username("Ned Stark").build();
    }
}
