package gov.sg.tech.domain;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ManageSessionRequestMessage {

    @Nonnull
    private Long userId;

    @NotBlank
    private String operationType;
}
