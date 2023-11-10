package gov.sg.tech.service;

import gov.sg.tech.domain.RegisterUserRequest;
import gov.sg.tech.domain.UserResponse;

public interface UserService {

    UserResponse registerUser(RegisterUserRequest registerUserRequest);
}
