CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    text TEXT NOT NULL,
    created TIMESTAMP NOT NULL,
    account_id INT NOT NULL REFERENCES account(id)
);