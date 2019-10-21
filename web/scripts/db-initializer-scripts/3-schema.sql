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
    birth_year INT NOT NULL,
    sex INT NOT NULL,                   -- enumerated {male, female, unknown}
    is_pregnant BOOLEAN NOT NULL,
    gestational_age INT,
    medical_history VARCHAR(255) DEFAULT '',
    drug_history VARCHAR(255) DEFAULT '',
    other_symptoms VARCHAR(255)
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

CREATE TABLE referral (
    id INT PRIMARY KEY AUTO_INCREMENT,
    pid VARCHAR(255) NOT NULL,
    vid INT NOT NULL,
    reading_id INT NOT NULL,
    health_centre VARCHAR(255),
    health_centre_number VARCHAR(255),
    comments VARCHAR(9001)
);
