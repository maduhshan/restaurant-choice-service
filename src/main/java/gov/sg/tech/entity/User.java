package gov.sg.tech.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String username;

    private String restaurantChoice;

    private boolean isWinner;

    @ManyToOne
    @JoinColumn(name="session_id", nullable=false)
    private Session session;
}
