-- 1) Morador
INSERT INTO tb_morador (
    id, nome, cpf, data_nascimento, celular, email, contatos_familia, senha
) VALUES (
    1,
    'Emmanuel Diniz Cheroto',
    '12345678900',
    DATE '2000-01-15',
    '31999990000',
    'emmanuel@example.com',
    '(31)98888-7777',
    '$2a$10$abcdefg'
);

-- 2) Tipo de Conta
INSERT INTO tb_tipo_conta (
    id, descricao, observacao
) VALUES (
    1,
    'Aluguel',
    'Aluguel mensal da república'
);

-- 3) Conta
INSERT INTO tb_conta (
    id,
    observacao,
    valor,
    data_vencimento,
    responsavel_id,
    tipo_conta_id,
    situacao
) VALUES (
    1,
    'Aluguel junho/2025',
    1200.00,
    DATE '2025-06-05',
    1,
    1,
    'PENDENTE'
);

-- 4) Rateio
INSERT INTO tb_rateio (
    id,
    valor,
    status,
    conta_id,
    morador_id
) VALUES (
    1,
    1200.00,
    'PENDENTE',
    1,
    1
);

-- 5) Histórico de Conta
INSERT INTO tb_historico_conta (
    id,
    conta_id,
    morador_id,
    acao,
    timestamp
) VALUES (
    1,
    1,
    1,
    'PENDENTE',
    TIMESTAMP '2025-06-01 09:00:00'
);

-- 6) Reinicia as sequências identity para cada tabela
ALTER TABLE tb_morador          ALTER COLUMN id RESTART WITH 2;
ALTER TABLE tb_tipo_conta      ALTER COLUMN id RESTART WITH 2;
ALTER TABLE tb_conta           ALTER COLUMN id RESTART WITH 2;
ALTER TABLE tb_rateio          ALTER COLUMN id RESTART WITH 2;
ALTER TABLE tb_historico_conta ALTER COLUMN id RESTART WITH 2;
