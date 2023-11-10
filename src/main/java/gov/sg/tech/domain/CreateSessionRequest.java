package gov.sg.tech.domain;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateSessionRequest {

    @NotBlank
    private String sessionName;

    @Nonnull
    private Long sessionOwnerId;

}
