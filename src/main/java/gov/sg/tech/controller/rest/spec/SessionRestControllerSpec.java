package gov.sg.tech.controller.rest.spec;

import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.ErrorResponse;
import gov.sg.tech.domain.dto.SessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenAPI Specs for Session Resource
 *
 * @author Madushan
 */
@Tag(name = "Session REST Controller",
        description = "This Document specifies REST Endpoints for Session Resource")
public interface SessionRestControllerSpec {

    @Operation(summary = "Create session")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = SessionResponse.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request when invalid inputs provided",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Not found when related session owner unavailable",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    ResponseEntity<SessionResponse> createSession(
            @RequestBody(description = "Create session request payload") CreateSessionRequest createSessionRequest);

    @Operation(summary = "Get session by Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = SessionResponse.class),
                            mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Not found when related session unavailable",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class))})})
    ResponseEntity<SessionResponse> getSession(@PathVariable Long id);
}
