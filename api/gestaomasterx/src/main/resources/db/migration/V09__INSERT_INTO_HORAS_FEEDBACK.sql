INSERT INTO `gestaomasterx_db`.`registro_horas` (`id_funcionario`, `data`, `hora_entrada`, `hora_saida`, `horas_trabalhadas`) VALUES
(101, '2024-07-28', '09:00:00', '17:00:00', 8.00),
(102, '2024-07-28', '09:00:00', '17:00:00', 8.00),
(103, '2024-07-28', '09:00:00', '17:00:00', 8.00),
(104, '2024-07-28', '08:30:00', '17:30:00', 9.00),
(105, '2024-07-28', '09:15:00', '18:00:00', 8.75),
(106, '2024-07-28', '08:45:00', '17:15:00', 8.50),
(101, '2024-07-29', '09:00:00', '17:00:00', 8.00),
(102, '2024-07-29', '09:00:00', '17:00:00', 8.00),
(103, '2024-07-29', '09:00:00', '17:00:00', 8.00),
(104, '2024-07-29', '08:30:00', '17:30:00', 9.00),
(105, '2024-07-29', '09:15:00', '18:00:00', 8.75),
(106, '2024-07-29', '08:45:00', '17:15:00', 8.50);

-- Insere dados na tabela feedback
INSERT INTO `gestaomasterx_db`.`feedback` (`id_funcionario`, `id_gestor`, `data`, `comentario`, `meta`) VALUES
(101, 201, '2024-07-28', 'Bom desempenho em tarefas de desenvolvimento.', 'Melhorar a comunicação com a equipe.'),
(102, 202, '2024-07-28', 'Ótimo trabalho nas campanhas gráficas.', 'Aumentar a eficiência na entrega dos projetos.'),
(103, 201, '2024-07-28', 'Desempenho satisfatório em tarefas administrativas.', 'Trabalhar na gestão de tempo.');
