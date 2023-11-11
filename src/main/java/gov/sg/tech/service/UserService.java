package gov.sg.tech.service;

import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.pojo.UserData;

/**
 * Service layer logics specs to serve user operations
 *
 * @author Madushan
 */
public interface UserService {

    /**
     * Provides the capability to register and user
     *
     * @param registerUserRequest Requested user registration details
     * @return Registered User in a Pojo
     */
    UserData registerUser(RegisterUserRequest registerUserRequest);
}
