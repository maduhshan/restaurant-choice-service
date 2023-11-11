-- Changeset madushan:002
CREATE TABLE session_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    restaurant_choice VARCHAR(255),
    is_owner BOOLEAN,
    session_id BIGINT(36),
    FOREIGN KEY (session_id) REFERENCES session(id)
);
