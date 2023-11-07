package gov.sg.tech.service;

import gov.sg.tech.domain.*;

public interface SessionService {

    SessionResponse getSessionById(String id);

    SessionResponse createSession(CreateSessionRequest createSessionRequest);

    SessionResponse joinSession(String id, JoinSessionRequest joinSessionRequest);

    SessionResponse manageSession(String id, String userId, ManageSessionOperationType operationType, Long length);

    SessionResponse manageSession(String id, ManageSessionRequestMessage sessionRequestMessage);
}
