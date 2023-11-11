package gov.sg.tech.controller.rest;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.dto.CreateSessionRequest;
import gov.sg.tech.domain.dto.SessionResponse;
import gov.sg.tech.service.SessionService;
import gov.sg.tech.controller.transformer.SessionDataTransformer;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionRestController {

    private final SessionService sessionService;

    private final SessionDataTransformer sessionDataTransformer;

    @ControllerLogger
    @PostMapping
    public ResponseEntity<SessionResponse> createSession(
            @RequestBody @Valid CreateSessionRequest createSessionRequest) {
        SessionResponse sessionResponse = sessionDataTransformer
                .transformToSessionResponse(sessionService.createSession(createSessionRequest));
        return new ResponseEntity<>(sessionResponse, HttpStatus.CREATED);
    }

    @ControllerLogger
    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable Long id) {
        SessionResponse sessionResponse = sessionDataTransformer
                .transformToSessionResponse(sessionService.getSessionById(id));
        return new ResponseEntity<>(sessionResponse, HttpStatus.OK);
    }
}
