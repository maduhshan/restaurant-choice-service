package gov.sg.tech.transformer;

import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.entity.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class UserDataTransformer {

    @Valid
    public UserResponse transformToFullUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getId())
                .username(user.getName())
                .restaurantChoice(user.getRestaurantChoice())
                .build();
    }

    public User transformToUser(String username, boolean isOwner) {
        return User.builder()
                .isOwner(isOwner)
                .name(username)
                .build();
    }
}
