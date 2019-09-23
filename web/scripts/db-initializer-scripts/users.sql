--
-- This file creates MySQL users upon startup of a new database service.
--

CREATE USER 'admin'@'%'
    IDENTIFIED BY 'super-strong-password';
GRANT ALL
    ON *.*
    TO 'admin'@'%'
    WITH GRANT OPTION;
