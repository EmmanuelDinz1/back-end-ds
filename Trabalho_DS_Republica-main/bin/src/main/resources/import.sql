INSERT INTO tb_pessoa (id, nome, cpf) VALUES (1, 'Emmanuel Diniz Cheroto', '12345678900');

INSERT INTO tb_conta (id, numero, saldo, pessoa_id) VALUES (1, '0001-9', 0.0, 1);

INSERT INTO tb_lancamento (id, valor, conta_id) VALUES (1, 250.0, 1);
INSERT INTO tb_lancamento (id, valor, conta_id) VALUES (2, -100.0, 1);
