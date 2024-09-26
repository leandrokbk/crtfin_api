create table transacoes(

    id bigint not null auto_increment,
    data DATE NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    tipo VARCHAR(40) NOT NULL,

    primary key(id)

);