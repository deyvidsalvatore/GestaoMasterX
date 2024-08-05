INSERT INTO `gestaomasterx_db`.`usuario` (`usuario_id`, `username`, `senha`, `role`) VALUES
(201, 'joao.silva', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'GESTOR'),
(202, 'ana.costa', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'GESTOR'),
(101, 'carlos.oliveira', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'FUNCIONARIO'),
(102, 'maria.santos', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'FUNCIONARIO'),
(103, 'pedro.almeida', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'FUNCIONARIO'),
(301, 'juliana.pereira', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'ADMINISTRADOR'),
(203, 'luan.martins', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'GESTOR'),
(204, 'carla.morais', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'GESTOR'),
(104, 'lucas.silveira', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'FUNCIONARIO'),
(105, 'beatriz.martins', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'FUNCIONARIO'),
(106, 'andre.souza', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'FUNCIONARIO'),
(302, 'paula.gomes', '$2a$12$hdf3XSGhs80DdsLxrTbA2.lS97SY2DZH5OMf.di9zL.gPZJa/v05O', 'ADMINISTRADOR');

INSERT INTO `gestaomasterx_db`.`funcionario` (`funcionario_id`, `nome_completo`, `cargo`, `email`, `usuario_id`) VALUES
(201, 'João Silva', 'Gestor de TI', 'joao.silva@exemplo.com', 201),
(202, 'Ana Costa', 'Gestora de Marketing', 'ana.costa@exemplo.com', 202),
(101, 'Carlos Oliveira', 'Analista de Sistemas', 'carlos.oliveira@exemplo.com', 101),
(102, 'Maria Santos', 'Designer Gráfico', 'maria.santos@exemplo.com', 102),
(103, 'Pedro Almeida', 'Assistente Administrativo', 'pedro.almeida@exemplo.com', 103),
(300, 'Juliana Pereira', 'Diretora Geral', 'juliana.pereira@exemplo.com', 301),
(203, 'Luan Martins', 'Gestor de Projetos', 'luan.martins@exemplo.com', 203),
(204, 'Carla Morais', 'Gestora de Vendas', 'carla.morais@exemplo.com', 204),
(104, 'Lucas Silveira', 'Desenvolvedor de Software', 'lucas.silveira@exemplo.com', 104),
(105, 'Beatriz Martins', 'Especialista em SEO', 'beatriz.martins@exemplo.com', 105),
(106, 'André Souza', 'Assistente de RH', 'andre.souza@exemplo.com', 106),
(301, 'Paula Gomes', 'Gerente de Operações', 'paula.gomes@exemplo.com', 302);