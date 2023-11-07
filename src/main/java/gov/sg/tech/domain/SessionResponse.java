package gov.sg.tech.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SessionResponse {

    @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank
    private String sessionId;

    @NotBlank
    private String sessionName;

    private LocalDateTime createdAt;

    private Set<UserResponse> users;

    private String restaurantChoice;

    private boolean ended;
}
