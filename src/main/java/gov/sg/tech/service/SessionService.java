package gov.sg.tech.service;

import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.JoinSessionRequest;
import gov.sg.tech.domain.dto.ManageSessionRequest;
import gov.sg.tech.domain.dto.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.pojo.SessionData;

/**
 * Service layer logics specs to serve session operations
 *
 * @author Madushan
 */
public interface SessionService {

    /**
     * Get session by id
     *
     * @param id session id
     * @return SessionData
     */
    SessionData getSessionById(Long id);

    /**
     * Create Session with session owner
     *
     * @param createSessionRequest CreateSessionRequest
     * @return SessionData
     */
    SessionData createSession(CreateSessionRequest createSessionRequest);

    /**
     * Join user to an existing session
     *
     * @param id                 session id
     * @param joinSessionRequest join session request
     * @return SessionData
     */
    SessionData joinSession(Long id, JoinSessionRequest joinSessionRequest);

    /**
     * Submit a restaurant choice to a active session
     *
     * @param id            session id
     * @param choiceRequest request contains choce details
     * @return SessionData
     */
    SessionData submitRestaurantChoice(Long id, SubmitRestaurantChoiceRequest choiceRequest);

    /**
     * Manage a session such as ending a given session
     *
     * @param id                    seesion id
     * @param sessionRequestMessage request payload which contains the session management operation
     * @return SessionData
     */
    SessionData manageSession(Long id, ManageSessionRequest sessionRequestMessage);
}
