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
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);


-- Dummy Data

INSERT INTO user (user_id, password, role)
VALUES ('admin', 'admin', 'ADMIN');

INSERT INTO user  (user_id, password, role)
VALUES ('health', 'health', 'HEALTHWORKER');

INSERT INTO user  (user_id, password, role)
VALUES ('vht', 'vht', 'VHT');
