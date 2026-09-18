CREATE TABLE produto (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome        VARCHAR(255) NOT NULL UNIQUE,
    preco       DOUBLE PRECISION,
    romaneio_id BIGINT
);

CREATE TABLE romaneios (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    data        DATE
);