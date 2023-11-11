package gov.sg.tech.controller.rest;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.controller.rest.spec.UserRestControllerSpec;
import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.dto.UserResponse;
import gov.sg.tech.service.UserService;
import gov.sg.tech.controller.transformer.UserDataTransformer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Controller for User Resource
 *
 * @author Madushan
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserRestController implements UserRestControllerSpec {

    private final UserService userService;

    private final UserDataTransformer userDataTransformer;

    @Override
    @ControllerLogger
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid RegisterUserRequest registerUserRequest) {
        UserResponse userResponse = userDataTransformer
                .transformToFullUserResponse(userService.registerUser(registerUserRequest));
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}
