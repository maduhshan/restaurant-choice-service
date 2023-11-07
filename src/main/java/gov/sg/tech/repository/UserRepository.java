package gov.sg.tech.repository;

import gov.sg.tech.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    User findByUserIdAndSessionId(UUID userId, UUID sessionId);
}
