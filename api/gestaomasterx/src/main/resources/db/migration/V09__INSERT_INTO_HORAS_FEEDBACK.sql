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
