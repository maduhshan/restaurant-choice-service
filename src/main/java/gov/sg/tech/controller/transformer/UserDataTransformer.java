package gov.sg.tech.controller.transformer;

import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.domain.pojo.UserData;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

/**
 * This is a Transformer which transforms UserData Pojo to REST understandable
 * UserResponse Object
 *
 * @author Madushan
 */
@Component
public class UserDataTransformer {

    /**
     * Transforms UserData to UserResponse
     *
     * @param user user data pojo
     * @return transformed UserResponse
     */
    @Valid
    public UserResponse transformToFullUserResponse(@Nonnull UserData user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .restaurantChoice(user.getRestaurantChoice())
                .build();
    }
}
