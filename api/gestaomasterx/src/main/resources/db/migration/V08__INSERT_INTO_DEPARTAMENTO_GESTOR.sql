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