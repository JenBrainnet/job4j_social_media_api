CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    sender_id INT NOT NULL REFERENCES account(id),
    receiver_id INT NOT NULL REFERENCES account(id),
    text TEXT NOT NULL,
    created TIMESTAMP NOT NULL
);