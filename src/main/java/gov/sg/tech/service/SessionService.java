package gov.sg.tech.service;

import gov.sg.tech.domain.*;

public interface SessionService {

    SessionResponse getSessionById(Long id);

    SessionResponse createSession(CreateSessionRequest createSessionRequest);

    SessionResponse joinSession(Long id, JoinSessionRequest joinSessionRequest);

    SessionResponse submitRestaurantChoice(Long id, SubmitRestaurantChoiceRequest choiceRequest);

    SessionResponse manageSession(Long id, ManageSessionRequestMessage sessionRequestMessage);
}
