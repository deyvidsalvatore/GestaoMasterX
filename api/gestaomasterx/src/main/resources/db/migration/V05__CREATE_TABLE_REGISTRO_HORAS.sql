CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`registro_horas` (
  `id_registro` INT PRIMARY KEY AUTO_INCREMENT,
  `id_funcionario` INT,
  `data` DATE NOT NULL,
  `hora_entrada` TIME NOT NULL,
  `hora_saida` TIME NOT NULL,
  `horas_trabalhadas` DECIMAL(5, 2) NOT NULL,
  CONSTRAINT `fk_registro_horas_funcionario` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario`(`funcionario_id`)
);
