package gov.sg.tech.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResponse {

    @NotBlank
    private Long sessionId;

    private String sessionName;

    private LocalDateTime createdAt;

    private Set<UserResponse> users;

    private String restaurantChoice;

    private boolean ended;
}
