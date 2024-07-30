CREATE DATABASE IF NOT EXISTS `gestaomasterx_db`;
USE `gestaomasterx_db`;

CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`departamento` (
  `departamento_id` INT PRIMARY KEY,
  `nome` VARCHAR(50) NOT NULL,
  `descricao` TEXT NOT NULL,
  `recursos`TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`usuario` (
  `usuario_id` INT PRIMARY KEY,
  `username` VARCHAR(32) NOT NULL UNIQUE,
  `senha` VARCHAR(64) NOT NULL,
  `role` ENUM('FUNCIONARIO', 'GESTOR', 'ADMINISTRADOR')
);

CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`funcionario` (
  `funcionario_id` INT PRIMARY KEY,
  `nome_completo` VARCHAR(150) NOT NULL,
  `cargo` VARCHAR(40) NOT NULL,
  `email` VARCHAR(200) NOT NULL UNIQUE,
  `usuario_id` INT,
  CONSTRAINT `fk_funcionario_usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`usuario_id`)
);

CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`gestor_departamento` (
  `funcionario_id` INT,
  `departamento_id` INT,
  PRIMARY KEY (`funcionario_id`, `departamento_id`),
  CONSTRAINT `fk_gestor_departamento_funcionario` FOREIGN KEY (`funcionario_id`) REFERENCES `funcionario`(`funcionario_id`),
  CONSTRAINT `fk_gestor_departamento_departamento` FOREIGN KEY (`departamento_id`) REFERENCES `departamento`(`departamento_id`)
);

CREATE TABLE IF NOT EXISTS `gestaomasterx_db`.`registro_horas` (
  `id_registro` INT PRIMARY KEY AUTO_INCREMENT,
  `id_funcionario` INT,
  `data` DATE NOT NULL,
  `hora_entrada` TIME NOT NULL,
  `hora_saida` TIME NOT NULL,
  `horas_trabalhadas` DECIMAL(5, 2) NOT NULL,
  CONSTRAINT `fk_registro_horas_funcionario` FOREIGN KEY (`id_funcionario`) REFERENCES `funcionario`(`funcionario_id`)
);

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

-- Insere dados na tabela usuario com IDs de acordo com o padrão
INSERT INTO `gestaomasterx_db`.`usuario` (`usuario_id`, `username`, `senha`, `role`) VALUES
(201, 'joao.silva', 'senha123', 'GESTOR'),
(202, 'ana.costa', 'senha123', 'GESTOR'),
(101, 'carlos.oliveira', 'senha123', 'FUNCIONARIO'),
(102, 'maria.santos', 'senha123', 'FUNCIONARIO'),
(103, 'pedro.almeida', 'senha123', 'FUNCIONARIO'),
(301, 'juliana.pereira', 'senha123', 'ADMINISTRADOR');

-- Insere dados na tabela funcionario com IDs de acordo com o padrão
INSERT INTO `gestaomasterx_db`.`funcionario` (`funcionario_id`, `nome_completo`, `cargo`, `email`, `usuario_id`) VALUES
(201, 'João Silva', 'Gestor de TI', 'joao.silva@exemplo.com', 201),
(202, 'Ana Costa', 'Gestora de Marketing', 'ana.costa@exemplo.com', 202),
(101, 'Carlos Oliveira', 'Analista de Sistemas', 'carlos.oliveira@exemplo.com', 101),
(102, 'Maria Santos', 'Designer Gráfico', 'maria.santos@exemplo.com', 102),
(103, 'Pedro Almeida', 'Assistente Administrativo', 'pedro.almeida@exemplo.com', 103),
(300, 'Juliana Pereira', 'Diretora Geral', 'juliana.pereira@exemplo.com', 301);

-- Insere dados na tabela departamento
INSERT INTO `gestaomasterx_db`.`departamento` (`departamento_id`, `nome`, `descricao`, `recursos`) VALUES
(1, 'Recursos Humanos', 'Gerencia recrutamento, seleção e desenvolvimento de pessoal.', '10 computadores, 1 software de RH, 5 salas de reunião.'),
(2, 'TI', 'Suporte técnico e desenvolvimento de sistemas internos.', '50 computadores, 20 servidores, 10 softwares de gestão.'),
(3, 'Marketing', 'Desenvolvimento de estratégias de marketing e campanhas publicitárias.', '5 ferramentas de análise, 3 softwares de design.'),
(4, 'Finanças', 'Controle e planejamento financeiro da empresa.', '2 softwares de contabilidade, 1 sistema de auditoria.'),
(5, 'Operações', 'Gestão da produção e logística da empresa.', '30 máquinas, 5 veículos de transporte, 1 software de gestão de operações.');

-- Insere dados na tabela gestor_departamento
INSERT INTO `gestaomasterx_db`.`gestor_departamento` (`funcionario_id`, `departamento_id`) VALUES
(201, 2),  -- João Silva gerencia TI
(202, 3);  -- Ana Costa gerencia Marketing

-- Insere dados na tabela registro_horas
INSERT INTO `gestaomasterx_db`.`registro_horas` (`id_funcionario`, `data`, `hora_entrada`, `hora_saida`, `horas_trabalhadas`) VALUES
(101, '2024-07-28', '09:00:00', '17:00:00', 8.00),
(102, '2024-07-28', '09:00:00', '17:00:00', 8.00),
(103, '2024-07-28', '09:00:00', '17:00:00', 8.00);

-- Insere dados na tabela feedback
INSERT INTO `gestaomasterx_db`.`feedback` (`id_funcionario`, `id_gestor`, `data`, `comentario`, `meta`) VALUES
(101, 201, '2024-07-28', 'Bom desempenho em tarefas de desenvolvimento.', 'Melhorar a comunicação com a equipe.'),
(102, 202, '2024-07-28', 'Ótimo trabalho nas campanhas gráficas.', 'Aumentar a eficiência na entrega dos projetos.'),
(103, 201, '2024-07-28', 'Desempenho satisfatório em tarefas administrativas.', 'Trabalhar na gestão de tempo.');
