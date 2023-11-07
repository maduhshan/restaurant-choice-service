package gov.sg.tech.controller.rest;

import gov.sg.tech.aspect.ControllerLogger;
import gov.sg.tech.domain.*;
import gov.sg.tech.exception.BadRequestException;
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

    @ControllerLogger
    @PostMapping("/{id}/join")
    public ResponseEntity<SessionResponse> joinSession(@PathVariable String id,
                                                       @RequestBody @Valid JoinSessionRequest joinSessionRequest) {
        return new ResponseEntity<>(sessionService.joinSession(id, joinSessionRequest), HttpStatus.OK);
    }

    @ControllerLogger
    @PostMapping("/{id}/user/{userId}/manageSession")
    public ResponseEntity<SessionResponse> manageSession(@PathVariable String id,
                                                         @PathVariable String userId,
                                                         @RequestParam(name = "operationType",
                                                             defaultValue = "end") Optional<String> operationType,
                                                         @RequestParam(name = "length") Long length) {
        var opType = operationType.orElseThrow(
                () -> new BadRequestException("Invalid manage session operation type"));
        var opEnum = ManageSessionOperationType.findByName(opType);
        var sessionResponse = sessionService.getSessionById(id);
        if (sessionResponse.isEnded() && opEnum.equals(END)) {
            return new ResponseEntity<>(sessionResponse, HttpStatus.CONFLICT);
        } else {
            var managedSession = sessionService.manageSession(id, userId, opEnum, length);
            return new ResponseEntity<>(managedSession, HttpStatus.OK);
        }
    }
}
