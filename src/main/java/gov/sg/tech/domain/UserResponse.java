package gov.sg.tech.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    @Nonnull
    private Long userId;

    @NotBlank
    private String username;

    private String restaurantChoice;

}
