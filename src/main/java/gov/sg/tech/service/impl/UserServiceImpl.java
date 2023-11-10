package gov.sg.tech.service.impl;

import gov.sg.tech.domain.RegisterUserRequest;
import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.entity.User;
import gov.sg.tech.repository.UserRepository;
import gov.sg.tech.service.UserService;
import gov.sg.tech.transformer.UserDataTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserDataTransformer userDataTransformer;

    @Transactional
    @Override
    public UserResponse registerUser(RegisterUserRequest registerUserRequest) {
        User user = userDataTransformer.transformToUser(registerUserRequest.getUsername(), false);
        log.info("Registering user: {}", user);
        return userDataTransformer.transformToFullUserResponse(userRepository.save(user));
    }
}
