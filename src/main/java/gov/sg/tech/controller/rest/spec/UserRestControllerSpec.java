package gov.sg.tech.controller.rest.spec;

import gov.sg.tech.domain.dto.ErrorResponse;
import gov.sg.tech.domain.dto.RegisterUserRequest;
import gov.sg.tech.domain.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

/**
 * OpenAPI Specs for User Resource
 *
 * @author Madushan
 */
@Tag(name = "User REST Controller",
     description = "This Document specifies REST Endpoints for User Resource")
public interface UserRestControllerSpec {

    @Operation(summary = "Register user")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = UserResponse.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request when invalid inputs provided",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    ResponseEntity<UserResponse> registerUser(
            @RequestBody(description = "Request payload to register user") RegisterUserRequest registerUserRequest);
}
