CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`funcionario` (
  `funcionario_id` INT PRIMARY KEY,
  `nome_completo` VARCHAR(150) NOT NULL,
  `cargo` VARCHAR(40) NOT NULL,
  `email` VARCHAR(200) NOT NULL UNIQUE,
  `usuario_id` INT,
  CONSTRAINT `fk_funcionario_usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`usuario_id`)
);