package gov.sg.tech.service.impl;

import gov.sg.tech.domain.*;
import gov.sg.tech.entity.Session;
import gov.sg.tech.entity.User;
import gov.sg.tech.exception.BadRequestException;
import gov.sg.tech.exception.ConflictOperationException;
import gov.sg.tech.exception.ResourceNotFoundException;
import gov.sg.tech.repository.SessionRepository;
import gov.sg.tech.repository.UserRepository;
import gov.sg.tech.service.SessionService;
import gov.sg.tech.transformer.SessionDataTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final UserRepository userRepository;

    private final SessionDataTransformer sessionDataTransformer;

    @Transactional
    @Override
    public SessionResponse getSessionById(Long id) {
        return convertToSessionResponse(getSessionBySessionId(id));
    }

    @Transactional
    @Override
    public SessionResponse createSession(CreateSessionRequest createSessionRequest) {
        User user = userRepository.findById(createSessionRequest.getSessionOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Session session = sessionDataTransformer.transformToSessionEntity(createSessionRequest);
        user.setOwner(true);
        user.setSession(session);
        session.setUsers(Collections.singletonList(user));
        log.info("Creating the session with session owner: {}", user.getName());
        return sessionDataTransformer.transformToSessionResponse(sessionRepository.save(session));
    }

    @Transactional
    @Override
    public SessionResponse joinSession(Long id, JoinSessionRequest joinSessionRequest) {
        Session session = sessionRepository
                .findById(id).orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        if (session.isEnded() && (session.getUsers() == null || session.getUsers().size() == 0)) { // guard condition
            log.error("User trying to join to an ended session, session id: {}", session.getId());
            throw new BadRequestException("Session already ended or invalid session");
        }
        User user = userRepository
                .findById(joinSessionRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setSession(session);
        session.getUsers().add(user);
        userRepository.save(user);
        log.info("User joined to the session: {}, username: {}", session.getId(), user.getName());
        return sessionDataTransformer.transformToSessionResponse(sessionRepository.saveAndFlush(session));
    }

    @Transactional
    @Override
    public SessionResponse submitRestaurantChoice(Long id, SubmitRestaurantChoiceRequest choiceRequest) {
        User user = userRepository
                .findById(choiceRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (user.getRestaurantChoice() == null
                || !user.getRestaurantChoice().equals(choiceRequest.getRestaurantChoiceName())) {
            user.setRestaurantChoice(choiceRequest.getRestaurantChoiceName());
            userRepository.saveAndFlush(user);
        } else {
            log.error("User trying to submit the save restaurant choice, session: {}, user: {}", id, user.getName());
            throw new ConflictOperationException("Restaurant choice is already submitted");
        }
        Session session = getSessionBySessionId(id);
        return convertToSessionResponse(session);
    }

    @Override
    public SessionResponse manageSession(Long id, ManageSessionRequestMessage sessionRequestMessage) {
        var opEnum = ManageSessionOperationType.findByName(sessionRequestMessage.getOperationType());
        Session session = findMatchingSession(id, sessionRequestMessage.getUserId());
        if (session.isEnded() && opEnum.equals(ManageSessionOperationType.END)) { // // guard condition
            log.error("Error due to trying to ending an already ended session: {}", id);
            throw new ConflictOperationException("Session already ended");
        }
        if (validateSessionOwner(session, sessionRequestMessage.getUserId())) { // guard condition
            log.error("Ending is not allowed for the given user session: {}", id);
            throw new UnsupportedOperationException("Operation not allowed for given user");
        }
        if (opEnum.equals(ManageSessionOperationType.END)) {
            session.setEnded(true);
            User user = getRandomSelectedUser(session);
            user.setWinner(true);
            log.info("Selected user for the restaurant choice, user:{}", user.getName());
        }
        return sessionDataTransformer.transformToSessionResponse(sessionRepository.save(session));
    }

    private SessionResponse convertToSessionResponse(Session session) {
        return sessionDataTransformer.transformToSessionResponse(session);
    }

    private Session getSessionBySessionId(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Session not found"));
    }

    private Session findMatchingSession(Long id, Long userId) {
        return Optional.ofNullable(sessionRepository
                .findByIdAndUsers_Id(id, userId))
                .orElseThrow(() -> new ResourceNotFoundException("There's no matching session available for the User"));
    }

    private boolean validateSessionOwner(Session session, Long userId) {
        User sessionOwner = session.getUsers()
                .stream()
                .filter(User::isOwner)
                .findAny().orElseThrow(() -> new UnsupportedOperationException("Not allowed operation"));
        return !sessionOwner.getId().equals(userId);
    }

    private User getRandomSelectedUser(Session session) {
        List<User> users = session.getUsers();
        var random = new Random().nextInt(users.size());
        return users.get(random);
    }
}
