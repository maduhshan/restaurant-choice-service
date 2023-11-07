package gov.sg.tech.repository;

import gov.sg.tech.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, String> {

    Session findBySessionIdAndUserId(UUID sessionId, UUID userId);
}
