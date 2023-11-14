package gov.sg.tech.service;

import gov.sg.tech.dao.transformer.SessionDaoTransformer;
import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.JoinSessionRequest;
import gov.sg.tech.domain.dto.ManageSessionRequest;
import gov.sg.tech.domain.dto.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.pojo.SessionData;
import gov.sg.tech.domain.pojo.UserData;
import gov.sg.tech.entity.Session;
import gov.sg.tech.entity.User;
import gov.sg.tech.exception.BadRequestException;
import gov.sg.tech.exception.ConflictOperationException;
import gov.sg.tech.exception.OperationNotAllowedException;
import gov.sg.tech.exception.ResourceNotFoundException;
import gov.sg.tech.dao.repository.SessionRepository;
import gov.sg.tech.dao.repository.UserRepository;
import gov.sg.tech.service.impl.SessionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionDaoTransformer sessionDaoTransformer;

    @InjectMocks
    private SessionServiceImpl sessionService;

    @Test
    void testGetSessionById() {
        // given
        final var mockSession = getMockSessionWithUsers();
        final var expected = getStubSessionData();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(mockSession));

        // when
        final var actual = sessionService.getSessionById(1234L);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testGetSessionByIdWhenNotFound() {
        // given
        final var mockSession = mock(Session.class);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        final var exception = assertThrows(ResourceNotFoundException.class,
                () -> sessionService.getSessionById(1234L));

        // then
        assertEquals("Session not found", exception.getMessage());
    }

    @Test
    void testCreateSessionSuccess() {
        // given
        final var mockCreateSessionReq = getStubCreateSessionReq();
        final var mockUser = getMockUser(4L, "Jon Snow", true, "KFC");
        final var mockSession = getMockSession();
        final var expected = getStubSessionData();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(sessionDaoTransformer.transformToSessionEntity(any(CreateSessionRequest.class))).thenReturn(mockSession);
        when(sessionRepository.save(any(Session.class))).thenReturn(mockSession);

        // when
        final var actual = sessionService.createSession(mockCreateSessionReq);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testJoinSessionSuccessWhenNoUsersBefore() {
        // given
        final var mockUser = getMockUser(4L, "Jon Snow", true, "KFC");
        final var mockSession = getMockSession();
        final var expected = getStubSessionData();
        final var stubJoinSessionReq = JoinSessionRequest.builder()
                .userId(10L).build();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(sessionRepository.saveAndFlush(any(Session.class))).thenReturn(mockSession);

        // when
        final var actual = sessionService.joinSession(1234L, stubJoinSessionReq);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testJoinSessionSuccessWhenUsersAreThereBefore() {
        // given
        final var mockUser = getMockUser(10L, "Ned Stark", true, "KFC");
        final var mockSession = getMockSessionWithUsers();
        final var expected = getStubSessionDataWithMultipleUsers();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(sessionRepository.saveAndFlush(any(Session.class))).thenReturn(mockSession);

        // when
        final var actual = sessionService.joinSession(1234L, JoinSessionRequest.builder().userId(4L).build());

        // then
        assertNotNull(actual);
        assertEquals(expected.getUsers().size(), actual.getUsers().size());
    }

    @Test
    void testJoinSessionForAlreadyEndedSession() {
        // given
        final var mockSession = getMockSession();
        mockSession.setEnded(true);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(mockSession));

        // when
        final var actual = assertThrows(BadRequestException.class, () -> sessionService
                .joinSession(1234L, JoinSessionRequest.builder().userId(4L).build()));

        // then
        assertEquals("Session already ended or invalid session", actual.getMessage());
    }

    @Test
    void testSubmitRestaurantChoiceSuccessWhenNoSubmissionBefore() {
        // given
        final var mockUser = getMockUser(4L, "Jon Snow", true, null);
        final var mockSession = getMockSessionWithUsers();
        final var expected = getStubSessionData();
        final var stubSubmitRestaurantChoice = SubmitRestaurantChoiceRequest.builder()
                .userId(4L)
                .restaurantChoiceName("KFC").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(mockUser);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(mockSession));

        // when
        final var actual = sessionService.submitRestaurantChoice(1234L, stubSubmitRestaurantChoice);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSubmitRestaurantChoiceSuccessWithUpdatedRestaurantChoice() {
        // given
        final var mockUser = getMockUser(4L, "Jon Snow", true, "KFC");
        final var mockSession = getMockSessionWithUsers();
        final var expected = getStubSessionData();
        final var stubSubmitRestaurantChoice = SubmitRestaurantChoiceRequest.builder()
                .userId(4L)
                .restaurantChoiceName("Stuff'D").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(mockUser);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(mockSession));

        // when
        final var actual = sessionService.submitRestaurantChoice(1234L, stubSubmitRestaurantChoice);

        // then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void testSubmitRestaurantChoiceWithRestaurantName() {
        // given
        final var mockUser = getMockUser(4L, "Jon Snow", true, "KFC");
        final var stubSubmitRestaurantChoice = SubmitRestaurantChoiceRequest.builder()
                .userId(4L)
                .restaurantChoiceName("KFC").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mockUser));
        // when
        final var expectedException = assertThrows(ConflictOperationException.class,
                () -> sessionService.submitRestaurantChoice(1234L, stubSubmitRestaurantChoice));

        // then
        assertEquals(expectedException.getClass(), ConflictOperationException.class);
        assertEquals("Restaurant choice is already submitted", expectedException.getMessage());
    }

    @Test
    void testManageSessionSuccessOnEndSessionOperationWithMultipleUsers() {
        // given
        final var mockSession = getMockSession();
        final var sessionOwner = getMockUser(4L, "Jon Snow", true, "KFC");
        final var oneMoreAnotherUser = getMockUser(5L, "Daenerys Targaryen", false, "Coliins");
        mockSession.setUsers(List.of(sessionOwner, oneMoreAnotherUser));
        final var stubManageSessionReq = ManageSessionRequest.builder()
                .userId(4L)
                .operationType("end")
                .build();
        final var expected = getStubSessionDataWithMultipleUsers();
        when(sessionRepository.findByIdAndUsers_Id(anyLong(), anyLong())).thenReturn(mockSession);
        when(sessionRepository.save(eq(mockSession))).thenReturn(mockSession);

        // when
        final var actual = sessionService.manageSession(1234L, stubManageSessionReq);

        // then
        assertNotNull(actual);
        assertNotNull(actual.getRestaurantChoice()); // restaurant is selected
        assertEquals(expected.getUsers().size(), actual.getUsers().size());
    }

    @Test
    void testManageSessionFailureWithNonSessionOwner() {
        // given
        final var mockSession = getMockSession();
        final var sessionOwner = getMockUser(4L, "Jon Snow", true, "KFC");

        mockSession.setUsers(List.of(sessionOwner));
        final var stubManageSessionReq = ManageSessionRequest.builder()
                .userId(10L)
                .operationType("end")
                .build();
        final var expected = getStubSessionData();
        when(sessionRepository.findByIdAndUsers_Id(anyLong(), anyLong())).thenReturn(mockSession);

        // when
        final var expectedException = assertThrows(OperationNotAllowedException.class,
                () -> sessionService.manageSession(1234L, stubManageSessionReq));

        // then
        assertEquals(OperationNotAllowedException.class, expectedException.getClass());
        assertEquals("Operation not allowed for given user", expectedException.getMessage());
    }

    @Test
    void testManageSessionFailureOnAlreadyEndedSession() {
        // given
        final var mockSession = getMockSessionWithUsers();
        mockSession.setEnded(true);
        final var stubManageSessionReq = ManageSessionRequest.builder()
                .userId(4L)
                .operationType("end")
                .build();
        final var expected = getStubSessionData();
        when(sessionRepository.findByIdAndUsers_Id(anyLong(), anyLong())).thenReturn(mockSession);

        // when
        final var expectedException = assertThrows(ConflictOperationException.class,
                () -> sessionService.manageSession(1234L, stubManageSessionReq));

        // then
        assertEquals(ConflictOperationException.class, expectedException.getClass());
        assertEquals("Session already ended", expectedException.getMessage());
    }

    private User getMockUser(Long id, String name, boolean isOwner, String restaurant) {
        return User.builder()
                .id(id)
                .name(name)
                .isOwner(isOwner)
                .restaurantChoice(restaurant)
                .build();
    }

    private Session getMockSession() {
        return Session.builder()
                .id(1234L)
                .name("Monday Lunch")
                .ended(false)
                .build();
    }

    private Session getMockSessionWithUsers() {
        List<User> users = new ArrayList<>();
        users.add(getMockUser(4L, "Jon Snow", true, "KFC"));
        return Session.builder()
                .id(1234L)
                .name("Monday Lunch")
                .users(users)
                .ended(false)
                .build();
    }

    private CreateSessionRequest getStubCreateSessionReq() {
        return CreateSessionRequest.builder()
                .sessionName("Monday Lunch")
                .userId(4L)
                .build();
    }

    private SessionData getStubSessionData() {
        return SessionData.builder()
                .sessionId(1234L)
                .sessionName("Monday Lunch")
                .users(Collections.singleton(UserData.builder()
                        .userId(4L)
                        .username("Jon Snow")
                        .restaurantChoice("KFC")
                        .build()))
                .build();
    }

    private SessionData getStubSessionDataWithMultipleUsers() {
        return SessionData.builder()
                .sessionId(1234L)
                .sessionName("Monday Lunch")
                .users(Set.of(UserData.builder()
                                .userId(4L)
                                .username("Jon Snow")
                                .restaurantChoice("KFC")
                                .build(),
                        UserData.builder()
                                .userId(5L)
                                .username("Daenerys Targaryen")
                                .restaurantChoice("Collins")
                                .build()))
                .build();
    }
}
