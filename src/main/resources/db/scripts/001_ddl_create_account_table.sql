CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    time_zone TEXT NOT NULL,
    created TIMESTAMP NOT NULL
);