CREATE TABLE friendship (
    id SERIAL PRIMARY KEY,
    owner_id INT NOT NULL REFERENCES account(id),
    friend_id INT NOT NULL REFERENCES account(id),
    created TIMESTAMP NOT NULL,
    UNIQUE (owner_id, friend_id)
);