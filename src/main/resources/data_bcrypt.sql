INSERT INTO users (username, password, enabled)
    values ('user', '$2a$10$lugeq4PS.g4wllkNgdBGgOjWNF9v5PjbH6jIjccnuttRBGiIDVehG', true);
INSERT INTO users (username, password, enabled)
    values ('admin', '$2a$10$lugeq4PS.g4wllkNgdBGgOjWNF9v5PjbH6jIjccnuttRBGiIDVehG', true);

INSERT INTO authorities (username, authority)
    values ('user', 'ROLE_USER');
INSERT INTO authorities (username, authority)
    values ('admin', 'ROLE_ADMIN');
