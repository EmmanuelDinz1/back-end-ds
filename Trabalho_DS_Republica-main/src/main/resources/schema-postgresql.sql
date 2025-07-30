-- 1) Cria os tipos ENUM que o Hibernate vai usar:
CREATE TYPE situacao_conta AS ENUM ('PENDENTE','QUITADA','CANCELADA');
CREATE TYPE status_rateio AS ENUM ('PENDENTE','PAGO');
CREATE TYPE user_role AS ENUM ('ADMIN','USER');
