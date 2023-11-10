-- Changeset madushan:001
CREATE TABLE session (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    ended BOOLEAN
);