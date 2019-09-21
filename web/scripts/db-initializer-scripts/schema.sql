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


-- Dummy Data

INSERT INTO user (user_id, password)
VALUES ('admin', 'admin');

INSERT INTO user  (user_id, password)
VALUES ('health', 'health');

INSERT INTO user  (user_id, password)
VALUES ('vht', 'vht');
