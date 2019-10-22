--
-- This file defines the database schema.
--

USE cradlerest;

CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);

CREATE TABLE patient (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    village VARCHAR(255) NOT NULL,
    zone VARCHAR(255) NOT NULL,
    birth_year INT NOT NULL,
    sex INT NOT NULL,                   -- enumerated {male, female, unknown}
    medical_history VARCHAR(255) DEFAULT '',
    drug_history VARCHAR(255) DEFAULT '',
    last_updated DATETIME NOT NULL
);

CREATE TABLE reading (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pid VARCHAR(255) NOT NULL,
    systolic INT NOT NULL,
    diastolic INT NOT NULL,
    heart_rate INT NOT NULL,
    is_pregnant BOOLEAN NOT NULL,
    gestational_age INT,                -- in days
    colour INT NOT NULL,                -- enumerated {green, yellow_up, yellow_down, red_up, red_down}
    timestamp DATETIME NOT NULL,
    other_symptoms TEXT,
    FOREIGN KEY (pid) REFERENCES patient (id)
);

CREATE TABLE referral (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pid VARCHAR(255) NOT NULL,
    vid INT NOT NULL,
    reading_id INT NOT NULL,
    health_centre VARCHAR(255),
    health_centre_number VARCHAR(255),
    comments VARCHAR(9001)
);

CREATE TABLE symptom (
     id INT PRIMARY KEY,
     text VARCHAR(255) UNIQUE
);

CREATE TABLE symptom_reading_relation (
    sid INT NOT NULL,
    rid INT NOT NULL,
    PRIMARY KEY (sid, rid),
    FOREIGN KEY (sid) REFERENCES symptom (id),
    FOREIGN KEY (rid) REFERENCES reading (id) ON DELETE CASCADE
);


-- Statically Defined Data
INSERT INTO symptom VALUES (0, 'Headache');
INSERT INTO symptom VALUES (1, 'Blurred Vision');
INSERT INTO symptom VALUES (2, 'Abdominal Pain');
INSERT INTO symptom VALUES (3, 'Bleeding');
INSERT INTO symptom VALUES (4, 'Feverish');
INSERT INTO symptom VALUES (5, 'Unwell');

