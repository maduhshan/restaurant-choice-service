package gov.sg.tech.service.impl;

import gov.sg.tech.dao.transformer.UserDaoTransformer;
import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.pojo.UserData;
import gov.sg.tech.entity.User;
import gov.sg.tech.dao.repository.UserRepository;
import gov.sg.tech.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer logics impl specs to serve user operations
 *
 * @author Madushan
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserDaoTransformer userDaoTransformer;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public UserData registerUser(RegisterUserRequest registerUserRequest) {
        User user = userDaoTransformer.transformToUser(registerUserRequest.getUsername(), false);
        log.info("Registering user: {}", user);
        return buildUserData(userRepository.save(user));
    }

    /**
     * Builds User Data to be sent over other layers
     *
     * @param user user entity
     * @return User Data
     */
    private UserData buildUserData(User user) {
        return UserData.builder()
                .userId(user.getId())
                .username(user.getName())
                .restaurantChoice(user.getRestaurantChoice())
                .build();
    }
}
