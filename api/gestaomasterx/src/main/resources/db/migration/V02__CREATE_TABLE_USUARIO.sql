CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`usuario` (
  `usuario_id` INT PRIMARY KEY,
  `username` VARCHAR(32) NOT NULL UNIQUE,
  `senha` VARCHAR(64) NOT NULL,
  `role` ENUM('FUNCIONARIO', 'GESTOR', 'ADMINISTRADOR')
);