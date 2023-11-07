package gov.sg.tech.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sessionId;

    private String name;

    private boolean ended;

    private Timestamp createdAt;

    private Timestamp endedAt;

    private Long timeOut;

    @OneToMany(mappedBy ="session")
    private List<User> users;

    private User sessionOwner;

    private String selectedRestaurantChoice;
}
