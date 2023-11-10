package gov.sg.tech.controller.rest;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.*;
import gov.sg.tech.exception.BadRequestException;
import gov.sg.tech.service.UserService;
import gov.sg.tech.service.SessionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static gov.sg.tech.domain.ManageSessionOperationType.END;

@RestController
@AllArgsConstructor
@RequestMapping("/sessions")
public class SessionRestController {

    private final SessionService sessionService;

    @ControllerLogger
    @PostMapping
    public ResponseEntity<SessionResponse> createSession(
            @RequestBody @Valid CreateSessionRequest createSessionRequest) {
        return new ResponseEntity<>(sessionService.createSession(createSessionRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionResponse> getSession(@PathVariable Long id) {
        return new ResponseEntity<>(sessionService.getSessionById(id), HttpStatus.OK);
    }
}
