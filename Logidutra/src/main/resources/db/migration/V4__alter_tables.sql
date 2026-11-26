ALTER TABLE usuarios ADD CONSTRAINT uk_usuarios_login UNIQUE (login);
ALTER TABLE usuarios ALTER COLUMN role SET NOT NULL;