package gov.sg.tech.service.impl;

import gov.sg.tech.domain.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.entity.User;
import gov.sg.tech.repository.UserRepository;
import gov.sg.tech.service.RestaurantChoiceService;
import gov.sg.tech.transformer.UserDataTransformer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class RestaurantChoiceServiceImpl implements RestaurantChoiceService {

    private final UserRepository userRepository;

    private final UserDataTransformer userDataTransformer;

    @Transactional
    @Override
    public UserResponse submitRestaurantChoice(String id, SubmitRestaurantChoiceRequest choiceRequest) {
        User user = userRepository
                .findByUserIdAndSessionId(UUID.fromString(choiceRequest.getUserId()), UUID.fromString(id));
        user.setRestaurantChoice(choiceRequest.getRestaurantChoiceName());

        return userDataTransformer.transformToFullUserResponse(userRepository.save(user));
    }
}
