--liquibase formatted sql

--changeset Grigoryev_Pavel:2
INSERT INTO "user" (nickname, password, register_time)
VALUES ('Alice', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=', '2021-10-01 12:00:00'),
       ('Bob', 'PCEiodAmV6WqYbqGUEoRGwTEJd80cfO41I1251V/On4=', '2021-10-02 13:00:00'),
       ('Charlie', '+rbUNlW7ym5xJAc586rT+QULWgUZJKs6O9F6lnhSWnQ=', '2021-10-03 14:00:00');

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
