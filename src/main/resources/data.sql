CREATE DATABASE IF NOT EXISTS demo_db;
USE demo_db;



INSERT INTO otp_statuses(status) VALUES('CREATED');
INSERT INTO otp_statuses(status) VALUES('VALIDATED');
INSERT INTO otp_statuses(status) VALUES('EXPIRED');