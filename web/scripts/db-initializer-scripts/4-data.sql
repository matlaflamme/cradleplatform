--
-- This file populates the database with dummy data.
--

USE cradlerest;

-- To pre encode:
-- https://www.browserling.com/tools/bcrypt
INSERT INTO user (username, password, role) -- password: admin
VALUES ('admin',
        '{bcrypt}$2a$10$1aYZ3QtMTK9k930s54ROmumfKskM1xLM1UznBgzjx4Mxn4tseLS4i',
        'ROLE_ADMIN');

INSERT INTO user  (username, password, role) -- password: health
VALUES ('health',
        '{bcrypt}$2a$10$q26.1T6krvjYHT9Me3sqqeQld9uPviBqxVphDLuDLX8octw3iV8hC',
        'ROLE_HEALTHWORKER');

INSERT INTO user  (username, password, role)
VALUES ('vht',
        '{bcrypt}$2a$10$MMMLYEsDhbgEXRSg9agAL.NwSoXTcjfed980ClKAFu/nl/ol9W5xG',
        'ROLE_VHT');

INSERT INTO user  (username, password, role)
VALUES ('test',
        '{bcrypt}$2a$10$MOX4VmxWhj0rBEeD8JYFaODweciGexZggq0jiqGNBWIoPQZTf6KB2',
        'ROLE_ADMIN');


INSERT INTO patient
VALUES ('001',          -- id
        'Harumi Youko', -- name
        '1',            -- village number
        '1',            -- zone number
        1995,           -- date of birth
        1,              -- sex
        NULL,           -- medical history
        NULL,           -- drug history
        '2019-09-20 20:12:32',  -- last updated
        NULL            -- notes
        );

INSERT INTO patient
VALUES ('002',          -- id
        'Lloyd Xavier Mann', -- name
        '3',            -- village number
        '3',            -- zone number
        1984,           -- date of birth
        0,              -- sex
        'hospitalized for X, taking medication for Y', -- medical history
        'there is some history with some drugs', -- drug history
        '2019-09-20 20:12:32',
        NULL            -- notes
        );  -- last updated


INSERT INTO reading (pid, systolic, diastolic, heart_rate, is_pregnant, gestational_age, colour, timestamp, created_by)
VALUES ('001', 100, 80, 74, TRUE, 15, 0, '2019-09-20 20:12:32', 3);

INSERT INTO reading (pid, systolic, diastolic, heart_rate, is_pregnant, gestational_age, colour, timestamp, created_by)
VALUES ('001', 130, 90, 80, TRUE, 30, 1,' 2019-09-22 6:37:00', 3);

INSERT INTO reading (pid, systolic, diastolic, heart_rate, is_pregnant, colour, timestamp, created_by)
VALUES ('001', 130, 100, 80, FALSE, 2, '2019-09-24 12:31:34', 3);

-- INSERT INTO reading (pid, systolic, diastolic, heart_rate, is_pregnant, gestational_age, colour, timestamp)
-- VALUES ('002', 140, 80, 93, 2, FALSE, 0, 0, '2018-06-12 08:54:15');

INSERT INTO health_centre (name, phone_number, location, email)
VALUES ('Twilio', '+12053465536', 'London', 'fakeemail12345@gmail.com');

INSERT INTO health_centre (name, location,email, phone_number, alternate_phone_number)
VALUES ('SFU', 'Uganda', 'fakeemail123456@gmail.com', '+12053465538', '+12053465539');

INSERT INTO health_centre (name, phone_number, location, alternate_phone_number)
VALUES ('FSU',  '+12053465530', 'burnaby', '+12053465531');


INSERT INTO referral (referred_by, reading_id, referred_to, patient, timestamp)
VALUES (3, 3, '+12053465536', '001','2019-09-24 12:31:34');

INSERT INTO referral (referred_by, reading_id, referred_to, patient, timestamp)
VALUES (3, 1, '+12053465536', '001', '2019-09-24 12:31:34');

INSERT INTO referral (referred_by, reading_id, referred_to, patient, timestamp)
VALUES (3, 3, '+12053465536', '002','2019-09-24 12:31:34');

-- Statically Defined Data
INSERT INTO symptom VALUES (0, 'Headache');
INSERT INTO symptom VALUES (1, 'Blurred Vision');
INSERT INTO symptom VALUES (2, 'Abdominal Pain');
INSERT INTO symptom VALUES (3, 'Bleeding');
INSERT INTO symptom VALUES (4, 'Feverish');
INSERT INTO symptom VALUES (5, 'Unwell');
