package gov.sg.tech.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ManageSessionRequestMessage {

    @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank
    private String userId;

    @NotBlank
    private String operationType;

    private Long length;
}
