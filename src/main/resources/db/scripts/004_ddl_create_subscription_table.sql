CREATE TABLE subscription (
    id SERIAL PRIMARY KEY,
    subscriber_id INT NOT NULL REFERENCES account(id),
    subscribed_account_id INT NOT NULL REFERENCES account(id),
    created TIMESTAMP NOT NULL,
    UNIQUE (subscriber_id, subscribed_account_id)
);