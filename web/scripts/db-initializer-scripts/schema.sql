--
-- This file initializes the database schema and populates it with dummy data.
--

-- Database
CREATE DATABASE cradlerest;

USE cradlerest;


-- Schema

-- For mock api testing: NOT FOR PRODUCTION USE!!!
CREATE TABLE user (
    id INT PRIMARY KEY,
    user_id VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL
);


-- Dummy Data

INSERT INTO user (id, user_id, password, role)
VALUES (1, "admin", "admin", "Administrator");

INSERT INTO user  (id, user_id, password, role)
VALUES (2, "health", "health", "HealthWorker");

INSERT INTO user  (id, user_id, password, role)
VALUES (3, "vht", "vht", "VHT");
