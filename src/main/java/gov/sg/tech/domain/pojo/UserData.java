package gov.sg.tech.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserData {

    private Long userId;

    private String username;

    private String restaurantChoice;
}
