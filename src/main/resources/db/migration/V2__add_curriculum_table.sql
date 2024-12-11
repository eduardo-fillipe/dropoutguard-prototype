CREATE SCHEMA IF NOT EXISTS intelligence_hub;

CREATE TABLE intelligence_hub.curriculum
(
    id                    VARCHAR(64) NOT NULL,
    knowledge_database_id VARCHAR(64),
    school_register       VARCHAR(255),
    student_id            VARCHAR(255),
    grade                 DECIMAL,
    created_at            TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_curriculum PRIMARY KEY (id)
);

CREATE TABLE intelligence_hub.student
(
    id       VARCHAR(64) NOT NULL,
    name     VARCHAR(255),
    document VARCHAR(255) UNIQUE,
    CONSTRAINT pk_student PRIMARY KEY (id)
);

ALTER TABLE intelligence_hub.curriculum
    ADD CONSTRAINT FK_CURRICULUM_ON_STUDENT FOREIGN KEY (student_id) REFERENCES intelligence_hub.student (id);