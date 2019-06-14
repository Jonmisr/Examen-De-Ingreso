CREATE TABLE clima
    (
        dia BIGINT NOT NULL,
		clima VARCHAR NULL,
        PRIMARY KEY (dia)
    );

CREATE TABLE
    hibernate_sequence
    (
        next_val BIGINT
);

INSERT INTO hibernate_sequence (next_val) VALUES (1);