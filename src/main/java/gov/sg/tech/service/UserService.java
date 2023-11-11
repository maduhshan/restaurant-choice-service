package gov.sg.tech.service;

import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.pojo.UserData;

public interface UserService {

    UserData registerUser(RegisterUserRequest registerUserRequest);
}
