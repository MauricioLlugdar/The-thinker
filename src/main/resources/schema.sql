CREATE TABLE IF NOT EXISTS idea
(
    id SERIAL,
    title VARCHAR(64) NOT NULL,
    description VARCHAR(256),
    visibility VARCHAR(16) NOT NULL,
    PRIMARY KEY (id)
);