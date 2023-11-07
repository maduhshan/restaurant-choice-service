package gov.sg.tech.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateSessionRequest {

    @NotBlank
    private String sessionName;

    @Pattern(regexp="[\\w_\\.]+")
    @NotBlank(message = "Session owner is mandatory")
    private String sessionOwner;

    @NotBlank
    private Long sessionTime;
}
