package gov.sg.tech.service.impl;

import gov.sg.tech.dao.transformer.SessionDaoTransformer;
import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.JoinSessionRequest;
import gov.sg.tech.domain.dto.ManageSessionOperationType;
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
import gov.sg.tech.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service layer logics impl to serve session operations
 *
 * @author Madushan
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final SessionDaoTransformer sessionDaoTransformer;

    /**
     * {@inheritDoc}
     *
     */
    @Transactional
    @Override
    public SessionData getSessionById(Long id) {
        return buildSessionData(getSessionBySessionId(id));
    }

    /**
     * {@inheritDoc}
     *
     */
    @Transactional
    @Override
    public SessionData createSession(CreateSessionRequest createSessionRequest) {
        User user = getUserById(createSessionRequest.getSessionOwnerId());
        Session session = sessionDaoTransformer.transformToSessionEntity(createSessionRequest);
        user.setOwner(true);
        user.setSession(session);
        session.setUsers(Collections.singletonList(user));
        log.info("Creating the session with session owner: {}", user.getName());
        return buildSessionData(sessionRepository.save(session));
    }

    /**
     * {@inheritDoc}
     *
     */
    @Transactional
    @Override
    public SessionData joinSession(Long id, JoinSessionRequest joinSessionRequest) {
        Session session = getSessionBySessionId(id);
        if (session.isEnded()) { // guard condition
            log.error("User trying to join to an ended session, session id: {}", session.getId());
            throw new BadRequestException("Session already ended or invalid session");
        }
        User user = getUserById(joinSessionRequest.getUserId());
        user.setSession(session);
        if (session.getUsers() != null) {
            session.getUsers().add(user);
        } else {
            session.setUsers(Collections.singletonList(user));
        }
        userRepository.save(user);
        log.info("User joined to the session: {}, username: {}", session.getId(), user.getName());
        return buildSessionData(sessionRepository.saveAndFlush(session));
    }

    /**
     * {@inheritDoc}
     *
     */
    @Transactional
    @Override
    public SessionData submitRestaurantChoice(Long id, SubmitRestaurantChoiceRequest choiceRequest) {
        User user = getUserById(choiceRequest.getUserId());
        if (user.getRestaurantChoice() == null
                || !user.getRestaurantChoice().equals(choiceRequest.getRestaurantChoiceName())) {
            user.setRestaurantChoice(choiceRequest.getRestaurantChoiceName());
            userRepository.saveAndFlush(user);
        } else {
            log.error("User trying to submit the save restaurant choice, session: {}, user: {}", id, user.getName());
            throw new ConflictOperationException("Restaurant choice is already submitted");
        }
        Session session = getSessionBySessionId(id);
        return buildSessionData(session);
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public SessionData manageSession(Long id, ManageSessionRequest sessionRequestMessage) {
        var opEnum = ManageSessionOperationType.findByName(sessionRequestMessage.getOperationType());
        Session session = findMatchingSession(id, sessionRequestMessage.getUserId());
        if (opEnum.equals(ManageSessionOperationType.END)) {
            validateManageSessionOperation(sessionRequestMessage.getUserId(), opEnum, session);
            String selectedRestaurant = getRandomSelectedRestaurant(session);
            session.setEnded(true);
            session.setSelectedRestaurant(selectedRestaurant);
            log.info("Selected restaurant choice: {}", selectedRestaurant);
        } else {
            log.error("Un-supported session operation given, {}", opEnum.getValue());
            throw new UnsupportedOperationException("Un-supported session operation given. " +
                    "Currently only supports ending a session");
        }
        return buildSessionData(sessionRepository.save(session));
    }

    /**
     * This method contains guard conditions that validates if the user is permission to end the session.
     * Also, If the session is an active session. Only active sessions are allowed to be ended.
     *
     * @param userId Id of the requested user
     * @param opEnum operation name (END,EXTEND etc..)
     * @param session related session to the owner and given session id
     */
    private void validateManageSessionOperation(Long userId, ManageSessionOperationType opEnum,
                                                Session session) {
        if (validateSessionOwner(session, userId)) { // guard condition
            log.error("Ending is not allowed for the given user, session: {}", session.getId());
            throw new OperationNotAllowedException("Operation not allowed for given user");
        }
        if (session.isEnded() && opEnum.equals(ManageSessionOperationType.END)) { // // guard condition
            log.error("Error due to trying to ending an already ended session: {}", session.getId());
            throw new ConflictOperationException("Session already ended");
        }
    }

    /**
     * Get user by given user id
     *
     * @param sessionOwnerId id of the session owning user
     * @return User entity
     */
    private User getUserById(Long sessionOwnerId) {
        return userRepository.findById(sessionOwnerId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    /**
     * Get session by given session d
     *
     * @param id session id
     * @return Session entity
     */
    private Session getSessionBySessionId(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    /**
     * Find the matching session for the given session id and requested uer
     *
     * @param id session id
     * @param userId requested user id
     * @return Session entity
     */
    private Session findMatchingSession(Long id, Long userId) {
        return Optional.ofNullable(sessionRepository
                .findByIdAndUsers_Id(id, userId))
                .orElseThrow(() -> new ResourceNotFoundException("There's no matching session available for the User"));
    }

    /**
     * Validates if the requested user is the session owner
     *
     * @param session related session for both user and given session id
     * @param userId requested user id
     * @return returns if requested user is session owner or not
     */
    private boolean validateSessionOwner(Session session, Long userId) {
        User sessionOwner = session.getUsers()
                .stream()
                .filter(User::isOwner)
                // un-likely that we'll get a session with out an owner. validation on hypothetically
                .findAny().orElseThrow(() -> new OperationNotAllowedException("Not allowed operation"));
        return !sessionOwner.getId().equals(userId);
    }

    /**
     * Select a random restaurant choice from the submitted list in an active session
     *
     * @param session related session for both user and given session id
     * @return Selected random restaurant choice
     */
    private String getRandomSelectedRestaurant(Session session) {
        List<String> restaurantChoices = session.getUsers().stream()
                .map(User::getRestaurantChoice)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        var random = new Random().nextInt(restaurantChoices.size());
        return restaurantChoices.get(random);
    }

    /**
     * Builds Session Data Pojo
     *
     * @param session session
     * @return Session Data Pojo
     */
    private SessionData buildSessionData(Session session) {
        return SessionData.builder()
                .sessionId(session.getId())
                .sessionName(session.getName())
                .ended(session.isEnded())
                .restaurantChoice(session.getSelectedRestaurant())
                .users(session.getUsers().stream()
                        .map(this::buildUserData)
                        .collect(Collectors.toSet()))
                .build();
    }

    /**
     * Builds User Data
     *
     * @param user user
     * @return User Data
     */
    private UserData buildUserData(User user) {
        return UserData.builder()
                .userId(user.getId())
                .username(user.getName())
                .restaurantChoice(user.getRestaurantChoice())
                .build();
    }
}
