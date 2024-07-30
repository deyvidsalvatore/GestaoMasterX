CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`gestor_departamento` (
  `funcionario_id` INT,
  `departamento_id` INT,
  PRIMARY KEY (`funcionario_id`, `departamento_id`),
  CONSTRAINT `fk_gestor_departamento_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `funcionario`(`funcionario_id`),
  CONSTRAINT `fk_gestor_departamento_departamento` FOREIGN KEY (`departamento_id`) REFERENCES `departamento`(`departamento_id`)
);