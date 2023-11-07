package gov.sg.tech.service.impl;

import gov.sg.tech.domain.*;
import gov.sg.tech.entity.Session;
import gov.sg.tech.entity.User;
import gov.sg.tech.exception.BadRequestException;
import gov.sg.tech.exception.ConflictOperationException;
import gov.sg.tech.exception.ResourceNotFoundException;
import gov.sg.tech.repository.SessionRepository;
import gov.sg.tech.service.SessionService;
import gov.sg.tech.transformer.SessionDataTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;

    private final SessionDataTransformer sessionDataTransformer;

    @Override
    public SessionResponse getSessionById(String id) {
        return sessionDataTransformer.transformToSessionResponse(sessionRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException::new));
    }

    @Transactional
    @Override
    public SessionResponse createSession(CreateSessionRequest createSessionRequest) {
       Session session = sessionRepository.save(sessionDataTransformer.transformToSessionEntity(createSessionRequest));
       return sessionDataTransformer.transformToSessionResponse(session);
    }

    @Transactional
    @Override
    public SessionResponse joinSession(String id, JoinSessionRequest joinSessionRequest) {
        Session session = sessionRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (session.isEnded()) {
            throw new BadRequestException("Session already ended");
        }
        Session updatedSession = sessionRepository.save(session);
        return sessionDataTransformer.transformToSessionResponse(updatedSession);
    }

    @Override
    public SessionResponse manageSession(String id, String userId, ManageSessionOperationType operationType,
                                         Long length) {
        Session session = findMatchingSession(id, userId);

        if (validateSessionOwner(session, userId)) { // guard condition
            throw new UnsupportedOperationException("Operation not allowed for given user");
        }
        if (operationType.equals(ManageSessionOperationType.END)) {
            session.setEnded(true);
            session.setEndedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Singapore")).toInstant()));
            session.setSelectedRestaurantChoice(getSelectedUser(session).getRestaurantChoice());
        } else {
            session.setTimeOut(session.getTimeOut() + length);
        }
        return sessionDataTransformer.transformToSessionResponse(sessionRepository.save(session));
    }

    @Override
    public SessionResponse manageSession(String id, ManageSessionRequestMessage sessionRequestMessage) {
        var opEnum = ManageSessionOperationType.findByName(sessionRequestMessage.getOperationType());
        Session session = findMatchingSession(id, sessionRequestMessage.getUserId());
        if (session.isEnded() && opEnum.equals(ManageSessionOperationType.END)) { // // guard condition
            throw new ConflictOperationException("Session already ended");
        }
        if (validateSessionOwner(session, sessionRequestMessage.getUserId())) { // guard condition
            throw new UnsupportedOperationException("Operation not allowed for given user");
        }
        if (opEnum.equals(ManageSessionOperationType.END)) {
            session.setEnded(true);
            session.setEndedAt(Timestamp.from(ZonedDateTime.now(ZoneId.of("Asia/Singapore")).toInstant()));
            session.setSelectedRestaurantChoice(getSelectedUser(session).getRestaurantChoice());
        } else {
            session.setTimeOut(session.getTimeOut() + sessionRequestMessage.getLength());
        }
        return sessionDataTransformer.transformToSessionResponse(sessionRepository.save(session));

    }

    private Session findMatchingSession(String id, String userId) {
        return Optional.ofNullable(sessionRepository
                .findBySessionIdAndUserId(UUID.fromString(id), UUID.fromString(userId)))
                .orElseThrow(ResourceNotFoundException::new);
    }

    private boolean validateSessionOwner(Session session, String userId) {
        return !session.getSessionOwner().getUserId().toString().equals(userId);
    }

    private User getSelectedUser(Session session) {
        List<User> users = session.getUsers();
        var random = new Random().nextInt(users.size());
        return users.get(random);
    }
}
