package gov.sg.tech.service;

import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.JoinSessionRequest;
import gov.sg.tech.domain.dto.ManageSessionRequest;
import gov.sg.tech.domain.dto.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.pojo.SessionData;

public interface SessionService {

    SessionData getSessionById(Long id);

    SessionData createSession(CreateSessionRequest createSessionRequest);

    SessionData joinSession(Long id, JoinSessionRequest joinSessionRequest);

    SessionData submitRestaurantChoice(Long id, SubmitRestaurantChoiceRequest choiceRequest);

    SessionData manageSession(Long id, ManageSessionRequest sessionRequestMessage);
}
