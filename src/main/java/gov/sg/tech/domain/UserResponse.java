package gov.sg.tech.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String userId;

    private String username;

    private String restaurantChoice;

}
