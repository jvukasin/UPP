INSERT INTO naucna_oblast (sifra, naziv) values
(1,'biologija'),
(2,'fizika'),
(3,'hemija'),
(4,'matematika'),
(5,'arhitektura'),
(6,'elektrotehnika'),
(7,'geodezija'),
(8,'racunarstvo'),
(9,'medicina');

insert into role values (1, 'ROLE_USER');
insert into role values (2, 'ROLE_ADMIN');
insert into role values (3, 'ROLE_UREDNIK');
insert into role values (4, 'ROLE_RECENZENT');
insert into role values (5, 'ROLE_AUTOR');

insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Admin','vlada', 1, 'Srbija', 'cvetanovic9696@gmail.com', 'Novi Knezevac', 'Vladimir', '$2a$10$QAOKmXRKmsq3ifhxkVmbAuJq0cDnxnc7abfy00eK3k96l91qDQCUy', 'Cvetanovic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('User','vule', 1, 'Srbija', 'jovic.vukasin@gmail.com', 'Novi Sad', 'Vukasin', '$2a$10$SAQescpnqhTxAVN6i1h2ROsoNYrR8KnEbZtylPUSEfqOrL8c9KIxy', 'Jovic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Urednik','djo', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Nikola', '$2a$10$I4xaV2Eyy.k2NwRLMkBqBe2xbF1oTTtEpUZwMjOzZV3m6l47IaBjS', 'Djordjevic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Recenzent','milica', 1, 'Srbija', 'flylivedrive@gmail.com', 'Stepanovicevo', 'Milica', '$2a$10$5ja5KYx.kvOqeRQYNsvbEOPMSeRrzF8Ux4e93gqwnuOBRbPeSaA9m', 'Makaric', '');

insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Recenzent','mace', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Nikola', '$2a$10$C4UdUyXubN2d1I9BvBhtU.SeSes/Ai1GwFePVoXxm6xGijIBYXwB.', 'Malencic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Urednik','lazicy', 1, 'Srbija', 'flylivedrive@gmail.com', 'Sremska Kamenica', 'Milan', '$2a$10$76Yz6oANuJPZR3oAkBWa5.imWI6FC0Hp9GHIDORuBGfFaTSQSKFjK', 'Lazic', '');

insert into user_roles values ('vlada', 2);
insert into user_roles values ('vule', 1);
insert into user_roles values ('djo', 5);
insert into user_roles values ('milica', 4);
insert into user_roles values ('mace', 4);
insert into user_roles values ('lazicy', 5);

insert into user_nobl values ('djo', 1), ('djo', 2), ('djo', 3), ('djo', 4), ('djo', 5), ('djo', 6), ('milica', 1), ('milica', 2), ('milica', 3), ('milica', 4), ('milica', 5), ('milica', 6);
insert into user_nobl values ('mace', 1), ('mace', 2), ('mace', 3), ('mace', 4), ('lazicy', 5), ('lazicy', 6), ('lazicy', 7);


insert into privilege values (1, 'RECENZENTI_TASK');
insert into roles_privileges values (2,1);