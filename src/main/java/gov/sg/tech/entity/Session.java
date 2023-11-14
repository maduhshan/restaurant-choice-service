package gov.sg.tech.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "session")
@NoArgsConstructor
public class Session {

    @Id
    private Long id;

    private String name;

    private boolean ended;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> users;

    @Column(name = "selectedRestaurant")
    private String selectedRestaurant;
}
