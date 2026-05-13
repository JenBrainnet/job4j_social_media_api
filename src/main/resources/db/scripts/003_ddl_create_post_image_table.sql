CREATE TABLE post_image (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    path TEXT NOT NULL,
    post_id INT NOT NULL REFERENCES post(id)
);