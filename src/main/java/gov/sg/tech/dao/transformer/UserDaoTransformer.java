package gov.sg.tech.dao.transformer;

import gov.sg.tech.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDaoTransformer {

    public User transformToUser(String username, boolean isOwner) {
        return User.builder()
                .isOwner(isOwner)
                .name(username)
                .build();
    }
}
