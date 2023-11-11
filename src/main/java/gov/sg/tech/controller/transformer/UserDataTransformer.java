package gov.sg.tech.controller.transformer;

import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.UserData;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class UserDataTransformer {

    @Valid
    public UserResponse transformToFullUserResponse(@Nonnull UserData user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .restaurantChoice(user.getRestaurantChoice())
                .build();
    }
}
