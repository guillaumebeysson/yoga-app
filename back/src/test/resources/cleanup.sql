-- Delete all rows from all tables
DELETE FROM PARTICIPATE;
DELETE FROM SESSIONS;
DELETE FROM TEACHERS;
DELETE FROM USERS;
-- Add more tables as needed

-- Reset the auto-incrementing primary key value
ALTER TABLE PARTICIPATE AUTO_INCREMENT = 1;
ALTER TABLE SESSIONS AUTO_INCREMENT = 1;
ALTER TABLE TEACHERS AUTO_INCREMENT = 1;
ALTER TABLE USERS AUTO_INCREMENT = 1;
-- Add more tables as needed

INSERT INTO TEACHERS (first_name, last_name)
VALUES ('Margot', 'DELAHAYE'),
       ('Hélène', 'THIERCELIN');

INSERT INTO SESSIONS (name,description,`date`,teacher_id,created_at,updated_at)
VALUES ('Dance','Random','2023-04-05 10:13:16',1,'2023-04-04 10:13:16','2023-04-04 10:13:39'),
       ('Music','Andom','2023-04-06 10:13:16',2,'2023-04-04 10:14:08','2023-04-04 10:14:08');

INSERT INTO USERS (first_name, last_name, admin, email, password)
VALUES ('Admin', 'Admin', true, 'yoga@studio.com', '$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq'),
       ('Beysson', 'Guillaume', true, 'guillaume.beysson@gmail.com', '$2a$10$KO6NEGaaItJf5TArHLS5MurbgCjuyfahi5w7P2Hq4a2e1CTTiMj4C');


INSERT INTO PARTICIPATE (user_id,session_id) VALUES (1,1);