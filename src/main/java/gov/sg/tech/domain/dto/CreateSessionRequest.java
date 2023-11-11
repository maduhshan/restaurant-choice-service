package gov.sg.tech.domain.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateSessionRequest {

    @NotBlank
    private String sessionName;

    @Nonnull
    private Long sessionOwnerId;

}
