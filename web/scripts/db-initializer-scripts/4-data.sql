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

INSERT INTO user  (username, password, role)
VALUES ('adminvht',
        '{bcrypt}$2a$10$cDqOL/UNe5.4zGdR9EkMJOmUEq4RDuROMp.VFUAFBJX.tfGorrc/a',
        'ROLE_VHT,ROLE_ADMIN');

INSERT INTO user  (username, password, role)
VALUES ('vhthealth',
        '{bcrypt}$2a$10$V1w/4w9OAtvKT00Q92rPp.iuxO4s43HOKppp5BPSXT1OnIDFG.KgC',
        'ROLE_VHT,ROLE_HEALTH');

INSERT INTO user  (username, password, role)
VALUES ('adminhealth',
        '{bcrypt}$2a$10$Ytx8e2oCF7oidLZA.Bmi0.dtTZorDpijhmYV69sMyUIRKxfpw1rpy',
        'ROLE_ADMIN,ROLE_HEALTH');


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

INSERT INTO referral (pid, vid, reading_id, health_centre, health_centre_number)
VALUES ('001', 3, 1, 'Twilio', '+12053465536')