INSERT INTO public.tb_usuario(id, email, nome, senha) VALUES (DEFAULT, 'natan2624@gmail.com', 'Natan O. Pelizzoni', 'a1b2c3');
INSERT INTO public.tb_usuario(id, email, nome, senha) VALUES (DEFAULT, '178332@upf.br', 'Natan O. Pelizzoni', '3c2b1c');

INSERT INTO public.tb_tarefa(id, titulo, conteudo, usuario_id) VALUES (DEFAULT, 'Tarefa 1', 'Conteúdo da tarefa 1', 1);
INSERT INTO public.tb_tarefa(id, titulo, conteudo, usuario_id) VALUES (DEFAULT, 'Tarefa 2', NULL, 1);
INSERT INTO public.tb_tarefa(id, titulo, conteudo, usuario_id) VALUES (DEFAULT, 'Tarefa 3', 'Conteúdo da tarefa 3', 2);
INSERT INTO public.tb_tarefa(id, titulo, conteudo, usuario_id) VALUES (DEFAULT, 'Tarefa 4', NULL, 2);
INSERT INTO public.tb_tarefa(id, titulo, conteudo, usuario_id) VALUES (DEFAULT, 'Tarefa 5', 'Conteúdo da tarefa 5', 1);
INSERT INTO public.tb_tarefa(id, titulo, conteudo, usuario_id) VALUES (DEFAULT, 'Tarefa 6', 'Conteúdo da tarefa 6', 1);