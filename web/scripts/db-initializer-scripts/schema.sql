--
-- This file initializes the database schema and populates it with dummy data.
--

-- Database
CREATE DATABASE cradlerest;

USE cradlerest;


-- Schema

-- For mock api testing: NOT FOR PRODUCTION USE!!!
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE patient (
    id VARCHAR(255) PRIMARY KEY,
    village INT NOT NULL,
    initials VARCHAR(16) NOT NULL,
    dob DATE NOT NULL,
    sex INT NOT NULL,                   -- enumerated {male, female, unknown}
    is_pregnant BOOLEAN NOT NULL,
    gestational_age INT,                -- in weeks
    medical_history VARCHAR(255) DEFAULT '',
    drug_history VARCHAR(255) DEFAULT '',
    other_symptoms VARCHAR(255)         -- notes:
                                            -- for symptoms not defined in TABLE symptoms
                                            -- join on TABLE symptoms for enumerated symptoms
                                            -- NULL for none
);

CREATE TABLE reading (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pid VARCHAR(255) NOT NULL,
    systolic INT NOT NULL,
    diastolic INT NOT NULL,
    heart_rate INT NOT NULL,
    colour INT NOT NULL,                -- enumerated {green, yellow, red}
    date DATETIME NOT NULL,
    FOREIGN KEY (pid) REFERENCES patient (id)
);


-- Dummy Data

INSERT INTO user (user_id, password)
VALUES ('admin', 'admin');

INSERT INTO user  (user_id, password)
VALUES ('health', 'health');

INSERT INTO user  (user_id, password)
VALUES ('vht', 'vht');


INSERT INTO patient
VALUES ('001',          -- id
        1,              -- village number
        'AB',           -- initials
        '1995-12-25',   -- date of birth
        1,              -- sex
        TRUE,           -- is pregnant?
        16,             -- gestational age: weeks
        NULL,           -- medical history
        NULL,           -- drug history
        NULL);          -- other symptoms

INSERT INTO patient
VALUES ('002',          -- id
        3,              -- village number
        'CD',           -- initials
        '1984-04-07',   -- date of birth
        0,              -- sex
        FALSE,          -- is pregnant?
        NULL,           -- gestational age: weeks
        'hospitalized for X, taking medication for Y', -- medical history
        'there is some history with some drugs', -- drug history
        NULL);          -- other symptoms


INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, date)
VALUES ('001', 100, 80, 74, 0, '2019-09-20 20:12:32');

INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, date)
VALUES ('001', 130, 90, 80, 1, '2019-09-22 6:37:00');

INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, date)
VALUES ('001', 130, 100, 80, 2, '2019-09-24 12:31:34');

INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, date)
VALUES ('002', 140, 80, 93, 2, '2018-06-12 08:54:15')
