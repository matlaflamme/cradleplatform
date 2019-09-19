-- The following commands are executed on startup of the mysql container

-- Create Administrator User
CREATE USER 'admin'@'%'
    IDENTIFIED BY 'super-strong-password';
GRANT ALL
    ON *.*
    TO 'admin'@'%'
    WITH GRANT OPTION;

-- Create the database required by the CRADLE server
CREATE DATABASE cradlerest;
