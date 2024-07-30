CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`feedback` (
  `id_feedback` INT PRIMARY KEY AUTO_INCREMENT,
  `id_funcionario` INT,
  `id_gestor` INT,
  `data` DATE NOT NULL,
  `comentario` TEXT NOT NULL,
  `meta` TEXT,
  CONSTRAINT `fk_feedback_funcionario` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario`(`funcionario_id`),
  CONSTRAINT `fk_feedback_gestor` FOREIGN KEY (`id_gestor`) REFERENCES `funcionario`(`funcionario_id`)
);