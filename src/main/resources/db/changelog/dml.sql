--liquibase formatted sql

--changeset Grigoryev_Pavel:2
INSERT INTO "user" (nickname, password, register_time)
VALUES ('Alice', 'password123', '2021-10-01 12:00:00'),
       ('Bob', 'secret456', '2021-10-02 13:00:00'),
       ('Charlie', 'qwerty789', '2021-10-03 14:00:00');

INSERT INTO role (name, description)
VALUES ('ADMIN', 'Mega Powerful role'),
       ('BAMBOLIALO', 'I can change a world'),
       ('DEATH_KNIGHT', 'This role is sucks');

INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1),
       (1, 3),
       (2, 2),
       (3, 2),
       (3, 3)
