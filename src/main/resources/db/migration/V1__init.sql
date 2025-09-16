CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS role (
  id_rol BIGINT PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT
);

CREATE TABLE IF NOT EXISTS users (
  id_user BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  last_name TEXT NOT NULL,
  email TEXT NOT NULL,
  identity_number TEXT NOT NULL,
  date_born DATE,
  address TEXT,
  phone_number TEXT,
  id_role BIGINT NOT NULL REFERENCES role(id_rol),
  base_salary NUMERIC(12,2) NOT NULL,
  password TEXT,
  CONSTRAINT uq_users_email UNIQUE (email),
  CONSTRAINT uq_users_identity UNIQUE (identity_number)
);

INSERT INTO role (id_rol, name, description) VALUES
  (1,'ADMIN','Administrador'),
  (2,'ADVISOR','Asesor'),
  (3,'CLIENT','Cliente')
ON CONFLICT (id_rol) DO NOTHING;
