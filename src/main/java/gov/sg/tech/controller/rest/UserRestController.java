package gov.sg.tech.controller.rest;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.RegisterUserRequest;
import gov.sg.tech.domain.SubmitRestaurantChoiceRequest;
import gov.sg.tech.domain.UserResponse;
import gov.sg.tech.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserService userService;

    @ControllerLogger
    @PostMapping
    public UserResponse registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        return userService.registerUser(registerUserRequest);
    }
}
