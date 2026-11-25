CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO usuarios (nome, login, senha, idade, role)
VALUES ('admin', 'admin', crypt('admin123', gen_salt('bf', 10)), 30, 'ADMIN');

INSERT INTO usuarios (nome, login, senha, idade, role, status)
VALUES ('motorista1', 'motorista1', crypt('motorista123', gen_salt('bf', 10)), 28, 'USER', 'DISPONIVEL');