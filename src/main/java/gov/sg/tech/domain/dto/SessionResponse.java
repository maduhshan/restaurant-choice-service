package gov.sg.tech.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionResponse {

    @NotBlank
    private Long sessionId;

    private String sessionName;

    private Set<UserResponse> users;

    private String restaurantChoice;

    private boolean ended;
}
