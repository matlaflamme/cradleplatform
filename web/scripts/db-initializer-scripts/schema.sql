--
-- This file initializes the database schema and populates it with dummy data.
--

-- Database
CREATE DATABASE cradlerest;

USE cradlerest;

SET GLOBAL time_zone = '+02:00';
-- Schema

-- For mock api testing: NOT FOR PRODUCTION USE!!!
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
    birth_year INT NOT NULL,
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
    timestamp DATETIME NOT NULL,
    FOREIGN KEY (pid) REFERENCES patient (id)
);

-- Dummy Data
-- To pre encode:
-- https://www.browserling.com/tools/bcrypt
INSERT INTO user (username, password, role) -- password: admin
VALUES ('admin', '{bcrypt}$2a$10$1aYZ3QtMTK9k930s54ROmumfKskM1xLM1UznBgzjx4Mxn4tseLS4i', 'ROLE_ADMIN');
INSERT INTO user  (username, password, role) -- password: health
VALUES ('health', '{bcrypt}$2a$10$q26.1T6krvjYHT9Me3sqqeQld9uPviBqxVphDLuDLX8octw3iV8hC', 'ROLE_HEALTHWORKER');
INSERT INTO user  (username, password, role)
VALUES ('vht', '{bcrypt}$2a$10$MMMLYEsDhbgEXRSg9agAL.NwSoXTcjfed980ClKAFu/nl/ol9W5xG', 'ROLE_VHT');
INSERT INTO user  (username, password, role)
VALUES ('test', '{bcrypt}$2a$10$MOX4VmxWhj0rBEeD8JYFaODweciGexZggq0jiqGNBWIoPQZTf6KB2', 'ROLE_ADMIN');
INSERT INTO user  (username, password, role)


INSERT INTO patient
VALUES ('001',          -- id
        'Harumi Youko', -- name
        '1',            -- village number
        1995,           -- date of birth
        1,              -- sex
        TRUE,           -- is pregnant?
        16,             -- gestational age: weeks
        NULL,           -- medical history
        NULL,           -- drug history
        NULL);          -- other symptoms

INSERT INTO patient
VALUES ('002',          -- id
        'Lloyd Xavier Mann', -- name
        '3',              -- village number
        1984,           -- date of birth
        0,              -- sex
        FALSE,          -- is pregnant?
        NULL,           -- gestational age: weeks
        'hospitalized for X, taking medication for Y', -- medical history
        'there is some history with some drugs', -- drug history
        NULL);          -- other symptoms


INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, timestamp)
VALUES ('001', 100, 80, 74, 0, '2019-09-20 20:12:32');

INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, timestamp)
VALUES ('001', 130, 90, 80, 1, '2019-09-22 6:37:00');

INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, timestamp)
VALUES ('001', 130, 100, 80, 2, '2019-09-24 12:31:34');

INSERT INTO reading (pid, systolic, diastolic, heart_rate, colour, timestamp)
VALUES ('002', 140, 80, 93, 2, '2018-06-12 08:54:15')
