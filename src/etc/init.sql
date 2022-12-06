INSERT INTO person (gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES ('male', 'Eric', 'Dubuis', '1959-01-01', 'eric.dubuis@bfh.ch', 'professor', '827ccb0eea8a706c4c34a16891f84e7b', 'professor', true ) ON CONFLICT DO NOTHING;
INSERT INTO person (gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES ('female', 'Nicole', 'Herold', '1990-02-23', 'nicole.herold@students.bfh.ch', 'heron', '827ccb0eea8a706c4c34a16891f84e7b', 'student', true ) ON CONFLICT DO NOTHING;
INSERT INTO person (gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES ('male', 'Mac', 'Müller', '1990-02-23', 'mac.mueller@students.bfh.ch', 'mulk', '827ccb0eea8a706c4c34a16891f84e7b', 'student', true ) ON CONFLICT DO NOTHING;
INSERT INTO person (gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES ('female', 'Rebecca', 'Vogt', '1990-02-23', 'rebecca.vogt@students.bfh.ch', 'vogtr', '827ccb0eea8a706c4c34a16891f84e7b', 'student', true ) ON CONFLICT DO NOTHING;
INSERT INTO person (gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES ('female', 'Silvia', 'Locher', '1990-01-01', 'silvia.locher@bfh.ch', 'admin', '827ccb0eea8a706c4c34a16891f84e7b', 'administrator', true ) ON CONFLICT DO NOTHING;
INSERT INTO person (gender, firstname, lastname, date_of_birth, email, username, password, role, status) VALUES ('female', 'Heidi', 'Amrhein', '1990-01-01', 'heidi.amrhein@bfh.ch', 'professor2', '827ccb0eea8a706c4c34a16891f84e7b', 'professor', true ) ON CONFLICT DO NOTHING;

INSERT INTO student (matriculation_number, personal_id) VALUES ('123-456-78', 2) ON CONFLICT DO NOTHING;
INSERT INTO student (matriculation_number, personal_id) VALUES ('234-567-89', 3) ON CONFLICT DO NOTHING;
INSERT INTO student (matriculation_number, personal_id) VALUES ('345-678-90', 4) ON CONFLICT DO NOTHING;


INSERT INTO module (module_number, name, description, module_coordinator, ects) VALUES ('BTI1011', 'Programming with Java 1', 'Programming with Java.. lorem ipsum dolor sit amet...', 1, 4) ON CONFLICT DO NOTHING;
INSERT INTO module (module_number, name, description, module_coordinator, ects) VALUES ('BTI1012', 'Programming with Java 2', 'Programming with Java advanced.. lorem ipsum dolor sit amet...', 1, 4) ON CONFLICT DO NOTHING;
INSERT INTO module (module_number, name, description, module_coordinator, ects) VALUES ('BTI1121', 'Software Engineering', 'Software Engineering.. lorem ipsum dolor sit amet...', 1, 4) ON CONFLICT DO NOTHING;
INSERT INTO module (module_number, name, description, module_coordinator, ects) VALUES ('BTI1301', 'Web Programming', 'Web Programming.. lorem ipsum dolor sit amet...', 1, 4) ON CONFLICT DO NOTHING;

INSERT INTO module_run (module_id, semester, year, running) VALUES (1, 'autumn', 2019, true) ON CONFLICT DO NOTHING;
INSERT INTO module_run (module_id, semester, year, running) VALUES (2, 'spring', 2020, true) ON CONFLICT DO NOTHING;
INSERT INTO module_run (module_id, semester, year, running) VALUES (3, 'spring', 2020, false) ON CONFLICT DO NOTHING;
INSERT INTO module_run (module_id, semester, year, running) VALUES (4, 'autumn', 2018, false) ON CONFLICT DO NOTHING;
INSERT INTO module_run (module_id, semester, year, running) VALUES (1, 'autumn', 2021, true) ON CONFLICT DO NOTHING;
INSERT INTO module_run (module_id, semester, year, running) VALUES (2, 'autumn', 2021, true) ON CONFLICT DO NOTHING;

INSERT INTO enrollment (module_run_id, student_id, grade) VALUES (1, 1, 'A') ON CONFLICT DO NOTHING;
INSERT INTO enrollment (module_run_id, student_id, grade) VALUES (1, 2, 'A') ON CONFLICT DO NOTHING;
INSERT INTO enrollment (module_run_id, student_id, grade) VALUES (1, 3, 'A') ON CONFLICT DO NOTHING;
INSERT INTO enrollment (module_run_id, student_id, grade) VALUES (2, 3, 'C') ON CONFLICT DO NOTHING;

INSERT INTO teaching (module_run_id, personal_id) VALUES (1, 1) ON CONFLICT DO NOTHING;
INSERT INTO teaching (module_run_id, personal_id) VALUES (2, 1) ON CONFLICT DO NOTHING;
INSERT INTO teaching (module_run_id, personal_id) VALUES (3, 6) ON CONFLICT DO NOTHING;
INSERT INTO teaching (module_run_id, personal_id) VALUES (4, 1) ON CONFLICT DO NOTHING;
INSERT INTO teaching (module_run_id, personal_id) VALUES (4, 6) ON CONFLICT DO NOTHING;