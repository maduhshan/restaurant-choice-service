package gov.sg.tech.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddUserRequest {

    @Pattern(regexp="[\\w_\\.]+")
    @NotBlank(message = "Username is mandatory")
    private String username;
}
