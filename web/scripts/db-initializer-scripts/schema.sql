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
    name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL
);

CREATE TABLE reading (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pid VARCHAR(255) NOT NULL,
    systolic INT NOT NULL,
    diastolic INT NOT NULL,
    heart_rate INT NOT NULL,
    colour INT NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (pid) REFERENCES patient (id)
);


-- Dummy Data

INSERT INTO user (user_id, password)
VALUES ('admin', 'admin');

INSERT INTO user  (user_id, password)
VALUES ('health', 'health');

INSERT INTO user  (user_id, password)
VALUES ('vht', 'vht');


INSERT INTO patient (id, name, dob)
VALUES ('001', 'Test Patient', '1995-12-25');


INSERT INTO reading (id, pid, systolic, diastolic, heart_rate, colour)
VALUES (1, '001', 100, 80, 74, 1);

INSERT INTO reading (id, pid, systolic, diastolic, heart_rate, colour)
VALUES (2, '001', 130, 90, 80, 2);
