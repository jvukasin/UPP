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
('Admin','vlada', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Knezevac', 'Vladimir', '$2a$10$QAOKmXRKmsq3ifhxkVmbAuJq0cDnxnc7abfy00eK3k96l91qDQCUy', 'Cvetanovic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('User','vule', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Vukasin', '$2a$10$SAQescpnqhTxAVN6i1h2ROsoNYrR8KnEbZtylPUSEfqOrL8c9KIxy', 'Jovic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Urednik','djo', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Nikola', '$2a$10$I4xaV2Eyy.k2NwRLMkBqBe2xbF1oTTtEpUZwMjOzZV3m6l47IaBjS', 'Djordjevic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Recenzent','milica', 1, 'Srbija', 'flylivedrive@gmail.com', 'Stepanovicevo', 'Milica', '$2a$10$5ja5KYx.kvOqeRQYNsvbEOPMSeRrzF8Ux4e93gqwnuOBRbPeSaA9m', 'Makaric', '');

insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Recenzent','mace', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Nikola', '$2a$10$C4UdUyXubN2d1I9BvBhtU.SeSes/Ai1GwFePVoXxm6xGijIBYXwB.', 'Malencic', '');
insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula, casopis_id) values
('Urednik','lazicy', 1, 'Srbija', 'flylivedrive@gmail.com', 'Sremska Kamenica', 'Milan', '$2a$10$76Yz6oANuJPZR3oAkBWa5.imWI6FC0Hp9GHIDORuBGfFaTSQSKFjK', 'Lazic', '', 1),
('User', 'mika', 1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Mikan', '$2y$10$O2k2WP.7SWWQqDN.B9bcKeuWfiDHlDCFptKJ5xDNFsjtoZR4uC3Xm', 'Mikanic', NULL, NULL);

insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Autor','pera', 1, 'Srbija', 'flylivedrive@gmail.com', 'Beograd', 'Petar', '$2y$10$O2k2WP.7SWWQqDN.B9bcKeuWfiDHlDCFptKJ5xDNFsjtoZR4uC3Xm', 'Peric', 'dipl. knjizevnik');

insert into user (type, username, active, drzava, email, grad, ime, password, prezime, titula) values
('Recenzent','duca', 1, 'Srbija', 'flylivedrive@gmail.com', 'Niš', 'Dušan', '$2y$10$SGFVmCA7YE01W01k4QcfseAInEAjvP6Rcc2egPAZ11zyWBkgxULa.', 'Kenjić', ''),
('Recenzent','deki', 1, 'Srbija', 'flylivedrive@gmail.com', 'Vranje', 'Dejan', '$2y$10$dA.GXUojvHwGr2CMlZaSqeQIIzjnytLvxJwc2oDe.k9KtHjgtjy.y', 'Bordjoski', '');


insert into user_roles values ('vlada', 2);
insert into user_roles values ('vule', 1);
insert into user_roles values ('djo', 3);
insert into user_roles values ('milica', 4);
insert into user_roles values ('mace', 4);
insert into user_roles values ('lazicy', 3);
insert into user_roles values ('pera', 5);
insert into user_roles values ('duca', 4);
insert into user_roles values ('deki', 4);

insert into user_nobl values ('djo', 1), ('djo', 2), ('djo', 3), ('djo', 4), ('djo', 5), ('djo', 6), ('milica', 1), ('milica', 2), ('milica', 3), ('milica', 4), ('milica', 5), ('milica', 6);
insert into user_nobl values ('mace', 1), ('mace', 2), ('mace', 3), ('mace', 4), ('lazicy', 1), ('lazicy', 2), ('lazicy', 3), ('lazicy', 4), ('lazicy', 6), ('lazicy', 7);
insert into user_nobl values ('duca', 1), ('duca', 2), ('duca', 4), ('duca', 6), ('deki', 1), ('deki', 2), ('deki', 3), ('deki', 5);

insert into privilege values (1, 'RECENZENTI_TASK');
insert into roles_privileges values (2,1);

insert into casopis (id, naziv, aktivan, issn, clanarina, urednik_id, is_registered, seller_id) values (1, "Nauka generalno", 1, "111", "citaoci", "djo", true, 2);
insert into casopis (id, naziv, aktivan, issn, clanarina, urednik_id, is_registered, seller_id) values (2, "Test za autore", 1, "222", "autori", "djo", false, null);

insert into casopis_nobl (naucna_oblast_sifra, casopisi_id) values (1,1), (2,1), (3,1);
insert into casopis_recenzenti (recenzent_username, casopisi_id) values ("milica", 1), ("mace", 1), ("duca", 1), ("deki", 1);

insert into naucni_rad (id, title, key_term, paper_abstract, price, currency, magazine_id, science_field_sifra, pdf_name, autor_id) values (2, 'Kako prihvatiti odgovornost?', 'Kljucni pojam', 'Abstrakt', 20, 'USD', 1, 1, 'Naucna Centrala.pdf', 'pera');
insert into naucni_rad (id, title, key_term, paper_abstract, price, currency, magazine_id, science_field_sifra, pdf_name, autor_id) values (1, 'Mape uma', 'Kljucni pojam', 'Abstrakt', 10, 'USD', 1, 1, 'Naucna Centrala.pdf', 'pera');
insert into naucni_rad (id, title, key_term, paper_abstract, price, currency, magazine_id, science_field_sifra, pdf_name, autor_id) values (3, 'Svet fizike i nečega', 'kljucni kljucni pojam', 'Abstrakt za svet fizike', 19, 'USD', 1, 2, 'Naucna Centrala.pdf', 'pera');
insert into naucni_rad (id, title, key_term, paper_abstract, price, currency, magazine_id, science_field_sifra, pdf_name, autor_id) values (4, 'Moj naučni rad', 'rad, lorem, ipsum', 'Ovaj rad sadrži specifičan lorem ipsum tekst', 21, 'USD', 1, 3, 'Moj naucni rad.pdf', 'pera');
insert into naucni_rad (id, currency, key_term, paper_abstract, pdf_name, price, title, autor_id, magazine_id, science_field_sifra) values (5, NULL, NULL, NULL, 'Miks radova.pdf', '0', NULL, 'pera', NULL, NULL);


insert into clanarina values ('4', '1', '2020-02-26', 'vule', '1');
insert into koautor (id, drzava, email, grad, ime, prezime, naucni_rad_id) values (1, 'Srbija', 'flylivedrive@gmail.com', 'Novi Sad', 'Vukašin', 'Jović', 2);

insert into science_paper_reviewers values ('1', 'mace');
insert into science_paper_reviewers values ('1', 'milica');
insert into science_paper_reviewers values ('2', 'mace');
insert into science_paper_reviewers values ('2', 'milica');
insert into science_paper_reviewers values ('3', 'milica');
insert into science_paper_reviewers values ('3', 'mace');
insert into science_paper_reviewers values ('4', 'deki');
insert into science_paper_reviewers values ('4', 'duca');

