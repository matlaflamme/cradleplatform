--
-- This file defines the database schema.
--

USE cradlerest;

CREATE TABLE health_centre
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    phone_number         VARCHAR(255) UNIQUE NOT NULL,
    name                 VARCHAR(255) NOT NULL,
    location             VARCHAR(255) NOT NULL,
    email                VARCHAR(255),
    manager_phone_number VARCHAR(255)
);

CREATE TABLE user
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255)        NOT NULL,
    role     VARCHAR(255)        NOT NULL,
    active   BOOLEAN             NOT NULL DEFAULT 1,
    created  DATETIME            NOT NULL DEFAULT current_timestamp,
    modified DATETIME ON UPDATE current_timestamp,
    works_at INT,

    FOREIGN KEY (works_at)
        REFERENCES health_centre (id)
        ON DELETE SET NULL
);

CREATE TABLE patient
(
    id              VARCHAR(255) PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    village         VARCHAR(255),
    zone            VARCHAR(255),
    birth_year      INT          NOT NULL,
    sex             INT          NOT NULL, -- enumerated {male, female, unknown}
    medication      TEXT,
    medical_history TEXT,
    drug_history    TEXT,
    last_updated    DATETIME     NOT NULL,
    notes           TEXT,
    created_by      INT,

    FOREIGN KEY (created_by)
        REFERENCES user (id)
);

CREATE TABLE reading
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    pid             VARCHAR(255) NOT NULL,
    systolic        INT          NOT NULL,
    diastolic       INT          NOT NULL,
    heart_rate      INT          NOT NULL,
    is_pregnant     BOOLEAN      NOT NULL,
    gestational_age INT,                   -- in days
    colour          INT          NOT NULL, -- enumerated {green, yellow_up, yellow_down, red_up, red_down}
    timestamp       DATETIME     NOT NULL,
    other_symptoms  TEXT,
    notes           TEXT,
    created_by      INT          NOT NULL,

    FOREIGN KEY (pid)
        REFERENCES patient (id)
        ON DELETE CASCADE,

    FOREIGN KEY (created_by)
        REFERENCES user (id)
);

CREATE TABLE diagnosis
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    patient     VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    resolved    BOOLEAN NOT NULL DEFAULT 0,

    FOREIGN KEY (patient)
        REFERENCES patient (id)
        ON DELETE CASCADE
);

CREATE TABLE referral
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    reading_id  INT          NOT NULL,
    referred_to Int NOT NULL,
    referred_by VARCHAR(255) NOT NULL,
    patient     VARCHAR(255) NOT NULL,
    timestamp   DATETIME     NOT NULL,
    closed_by   INT,
    diagnosis   INT,
    closed      DATETIME,

    FOREIGN KEY (referred_by)
        REFERENCES user (username),

    FOREIGN KEY (referred_to)
        REFERENCES health_centre(id)
        ON DELETE CASCADE,

    FOREIGN KEY (reading_id)
        REFERENCES reading (id)
        ON DELETE CASCADE,

    FOREIGN KEY (closed_by)
        REFERENCES user (id),

    FOREIGN KEY (patient)
        REFERENCES patient (id)
        ON DELETE CASCADE,

    FOREIGN KEY (diagnosis)
        REFERENCES diagnosis (id)
        ON DELETE CASCADE
);

CREATE TABLE symptom
(
    id   INT PRIMARY KEY,
    text VARCHAR(255) UNIQUE
);

CREATE TABLE symptom_reading_relation
(
    sid INT NOT NULL,
    rid INT NOT NULL,

    PRIMARY KEY (sid, rid),

    FOREIGN KEY (sid)
        REFERENCES symptom (id),

    FOREIGN KEY (rid)
        REFERENCES reading (id)
        ON DELETE CASCADE
);