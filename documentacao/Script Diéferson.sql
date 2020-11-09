
-- -----------------------------------------------------
-- Schema estacionamento 
-- Autor: Diéferson Lopes
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `estacionamento`;

-- -----------------------------------------------------
-- Table `estacionamento`.`tb_cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estacionamento`.`tb_cliente` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cpf` VARCHAR(11) NULL DEFAULT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `nome` VARCHAR(255) NULL DEFAULT NULL,
  `telefone` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));


-- -----------------------------------------------------
-- Table `estacionamento`.`tb_veiculo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estacionamento`.`tb_veiculo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `placa` VARCHAR(255) NULL DEFAULT NULL,
  `veiculo` VARCHAR(255) NOT NULL,
  `cliente_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `unique_placa` (`placa` ASC),
    FOREIGN KEY (`cliente_id`)
    REFERENCES `estacionamento`.`tb_cliente` (`id`));


-- -----------------------------------------------------
-- Table `estacionamento`.`tb_ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estacionamento`.`tb_ticket` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `ativo` BIT(1) NOT NULL,
  `data_hora_entrada` DATETIME(6) NULL DEFAULT NULL,
  `data_hora_saida` DATETIME(6) NULL DEFAULT NULL,
  `valor_total` DOUBLE NOT NULL,
  `cliente_id` BIGINT NULL DEFAULT NULL,
  `veiculo_id` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `cliente_id` (`cliente_id` ASC),
  INDEX `veiculo_id` (`veiculo_id` ASC),
  CONSTRAINT `tb_ticket_ibfk_1`
    FOREIGN KEY (`cliente_id`)
    REFERENCES `estacionamento`.`tb_cliente` (`id`),
    FOREIGN KEY (`veiculo_id`)
    REFERENCES `estacionamento`.`tb_veiculo` (`id`))
