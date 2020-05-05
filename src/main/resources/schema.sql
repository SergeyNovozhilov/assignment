DROP TABLE IF EXISTS QUOTES;
CREATE TABLE QUOTES
(
    ID UUID PRIMARY KEY NOT NULL,
    ISIN VARCHAR(255) NOT NULL,
    BID FLOAT(5),
    ASK FLOAT(5),
    STMP TIMESTAMP NOT NULL
);

