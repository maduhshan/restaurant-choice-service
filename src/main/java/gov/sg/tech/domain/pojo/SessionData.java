package gov.sg.tech.domain.pojo;

import gov.sg.tech.domain.dto.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SessionData {

    private Long sessionId;

    private String sessionName;

    private Set<UserData> users;

    private String restaurantChoice;

    private boolean ended;
}
