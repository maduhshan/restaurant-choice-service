package gov.sg.tech.dao.transformer;

import gov.sg.tech.entity.User;
import org.springframework.stereotype.Component;

/**
 * Transforms user objects to DAO layer compatible entities
 *
 * @author Madushan
 */
@Component
public class UserDaoTransformer {

    /**
     * Transforms data in to user entity
     *
     * @param username name of the user
     * @param isOwner is user is session owner
     * @return transformed user entity
     */
    public User transformToUser(String username, boolean isOwner) {
        return User.builder()
                .isOwner(isOwner)
                .name(username)
                .build();
    }
}
