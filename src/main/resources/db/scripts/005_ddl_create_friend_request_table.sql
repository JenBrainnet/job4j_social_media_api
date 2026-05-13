CREATE TABLE friend_request (
    id SERIAL PRIMARY KEY,
    sender_id INT NOT NULL REFERENCES account(id),
    receiver_id INT NOT NULL REFERENCES account(id),
    status TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    UNIQUE (sender_id, receiver_id)
);