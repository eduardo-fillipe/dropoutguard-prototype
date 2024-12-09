CREATE TABLE knowledge_database
(
    id                      VARCHAR(255) NOT NULL,
    name                    VARCHAR(255),
    description             VARCHAR(255),
    register_file_location  VARCHAR(255),
    reason                  VARCHAR(255),
    cancellation_reason     VARCHAR(255),
    request_date_time       TIMESTAMP WITHOUT TIME ZONE,
    last_modified_date_time TIMESTAMP WITHOUT TIME ZONE,
    cancellation_date_time  TIMESTAMP WITHOUT TIME ZONE,
    status                  VARCHAR(255),
    CONSTRAINT pk_knowledge_database PRIMARY KEY (id)
);